package io.github.ompc.erniebot4j.executor;

import java.time.Duration;

public interface Request {

    Model model();

    Option options();

    Duration timeout();

    String user();

}
