package io.github.ompc.erniebot4j.executor;

public interface Response {

    String id();

    String type();

    long timestamp();

    Usage usage();

}
