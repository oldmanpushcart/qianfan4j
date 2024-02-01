package io.github.ompc.erniebot4j.executor;

public interface Response {

    String id();

    String type();

    long timestamp();

    Usage usage();

    /**
     * 用量
     *
     * @param prompt     提示用量
     * @param completion 完成用量
     * @param total      总用量
     */
    record Usage(int prompt, int completion, int total) {

        public Usage() {
            this(0, 0, 0);
        }

    }

}
