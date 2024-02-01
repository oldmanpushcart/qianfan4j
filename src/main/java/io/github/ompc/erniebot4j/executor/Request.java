package io.github.ompc.erniebot4j.executor;

import java.time.Duration;

public interface Request {

    Model model();

    String user();

    Option options();

    Duration timeout();

}
