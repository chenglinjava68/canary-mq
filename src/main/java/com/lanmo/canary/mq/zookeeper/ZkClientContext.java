package com.lanmo.canary.mq.zookeeper;

import com.google.common.collect.Lists;

import com.lanmo.canary.mq.common.CanaryConfig;
import com.lanmo.canary.mq.common.Const;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc
 * @date 2017/4/24
 */
public class ZkClientContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClientContext.class);

    private ZkConfig config;

    private CuratorFramework framework;

    private AtomicBoolean start = new AtomicBoolean(false);

    // 分布式锁
    private InterProcessMutex processMutex;

    // 子节点监听
    private List<PathChildrenCache> watchers;

    // 所有子节点监听
    private List<TreeCache> treeWatchers;

    // 节点数据监听
    private List<NodeCache> nodeWatchers;

    public ZkClientContext(ZkConfig config) {
        this.config = config;
    }

    public void start() throws InterruptedException {
        framework = CuratorFrameworkFactory.builder()
                                           .connectString(config.getZkServer())
                                           .connectionTimeoutMs(config.getZkServerConnectTimeout())
                                           .retryPolicy(new RetryNTimes(config.getZkRetryTimes(), config.getZkRetrySleepInterval()))
                                           .build();

        // 启动
        framework.start();

        processMutex = new InterProcessMutex(framework, Const.ZK_MQ_LOCK_PATH);

        CuratorFrameworkState state = framework.getState();
        framework.blockUntilConnected();
        if (state == CuratorFrameworkState.STARTED) {
            start.set(true);
            LOGGER.info("connect zk server[{}] success", config.getZkServer());
        }

        watchers = Lists.newLinkedList();
        treeWatchers = Lists.newLinkedList();
        nodeWatchers = Lists.newLinkedList();
    }

    public boolean isServing() {
        return start.get();
    }

    public void destroy() throws Exception {
        start.set(false);
        if (framework != null) {
            framework.close();
            framework = null;
        }

        if (watchers != null) {
            for (PathChildrenCache watcher : watchers) {
                watcher.close();
            }
        }

        if (treeWatchers != null) {
            treeWatchers.forEach(TreeCache::close);
        }

        if (nodeWatchers != null) {
            for (NodeCache watcher : nodeWatchers) {
                watcher.close();
            }
        }
    }

    // 仅对子节点监控
    public void addPathListener(String path, boolean cacheData, PathChildrenCacheListener listener) throws Exception {
        valid();
        PathChildrenCache watcher = new PathChildrenCache(framework, path, cacheData);
        watcher.getListenable().addListener(listener);
        watcher.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        watchers.add(watcher);
    }

    // 对所有子节点监控
    public void addSubAllPathListener(String path, TreeCacheListener listener) throws Exception {
        valid();
        TreeCache treeCache = new TreeCache(framework, path);
        treeCache.getListenable().addListener(listener);
        treeCache.start();
        treeWatchers.add(treeCache);
    }

    public NodeCache addNodeListener(String path, NodeCacheListener listener) throws Exception {
        valid();
        NodeCache nodeCache = new NodeCache(framework, path);
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
        nodeWatchers.add(nodeCache);
        return nodeCache;
    }

    public void updateNodeData(String path, byte[] data) {
        valid();
        try {
            if (isNodeExist(path)) {
                framework.setData().forPath(path, data);
            } else {
                createNode(path, data, CreateMode.EPHEMERAL);
            }

        } catch (Exception e) {
            LOGGER.error("Update zk node[{}] data exception", path, e);
        }

    }

    public byte[] getNodeData(String path) {
        try {
            valid();
            Stat stat = new Stat();
            return framework.getData().storingStatIn(stat).forPath(path);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isNodeExist(String path) throws Exception {
        valid();
        Stat stat = framework.checkExists().forPath(path);
        return stat != null;
    }

    public List<String> getChildNode(String parentPath) {
        valid();
        try {
            return framework.getChildren().forPath(parentPath);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public String createNode(String path, String data) throws Exception {
       return createNode(path, data, CreateMode.EPHEMERAL);
    }

    public String createNode(String path, String data, CreateMode mode) throws Exception {
        return createNode(path, data.getBytes(), mode);
    }

    public String createNode(String path, byte []data, CreateMode mode) throws Exception {
        valid();
        return framework.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
    }

    public void deleteNode(String path) {
        try {
            valid();
            Stat stat = framework.checkExists().forPath(path);
            if (stat != null) {
                framework.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            }
        } catch (Exception e) {
            LOGGER.error("delete zk path {} exception .", path, e);
        }

    }

    public InterProcessMutex getProcessMutex() {
        return processMutex;
    }

    private void valid() {
        if (!start.get()) {
            throw new IllegalStateException("already closed zk connect");
        }
    }

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        ZkConfig zkConfig = new ZkConfig(new CanaryConfig(props));
        ZkClientContext zkClientContext = new ZkClientContext(zkConfig);
        zkClientContext.start();

        zkClientContext.addSubAllPathListener("/test", new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("type : " + event.getType());
                ChildData data = event.getData();
                System.out.println("path : " + data.getPath());
                System.out.println("data : " + new String(data.getData()));
            }
        });

        for (int i = 0; i < 3; ++i) {
            zkClientContext.updateNodeData("/test/1/2", UUID.randomUUID().toString().getBytes());
        }

    }

}
