package io.github.ompc.erniebot4j.cloud.baidu.bos;

import io.github.ompc.erniebot4j.cloud.baidu.BceCredential;
import io.github.ompc.erniebot4j.cloud.baidu.BceRegion;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

/**
 * BOS客户端
 */
public interface BosClient {

    /**
     * 上传文件
     *
     * @param bucket 桶名
     * @param key    文件名
     * @param data   文件数据
     * @return 文件下载URL
     */
    CompletableFuture<URL> putBinary(String bucket, String key, byte[] data);

    /**
     * 上传PNG图片
     *
     * @param bucket 桶名
     * @param key    文件名
     * @param image  图片
     * @return 图片下载URL
     */
    CompletableFuture<URL> putPngImage(String bucket, String key, BufferedImage image);

    /**
     * BOS客户端构建器
     */
    class Builder {

        private BceCredential credential;
        private BceRegion region = BceRegion.BJ;
        private Executor executor;

        /**
         * 设置BCE凭证
         *
         * @param credential BCE凭证
         * @return this
         */
        public Builder credential(BceCredential credential) {
            this.credential = requireNonNull(credential);
            return this;
        }

        /**
         * 设置BCE区域
         *
         * @param region BCE区域
         * @return this
         */
        public Builder region(BceRegion region) {
            this.region = requireNonNull(region);
            return this;
        }

        /**
         * 设置线程池
         *
         * @param executor 线程池
         * @return this
         */
        public Builder executor(Executor executor) {
            this.executor = requireNonNull(executor);
            return this;
        }

        BceCredential credential() {
            return credential;
        }

        BceRegion region() {
            return region;
        }

        Executor executor() {
            return executor;
        }

        /**
         * 构建BOS客户端
         *
         * @return BOS客户端
         */
        public BosClient build() {
            return new BosClientImpl(this);
        }

    }

}
