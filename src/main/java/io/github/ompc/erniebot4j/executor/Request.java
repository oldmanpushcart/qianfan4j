package io.github.ompc.erniebot4j.executor;

import java.time.Duration;

public interface Request {

    Model model();

    Option option();

    Duration timeout();

    String user();

}
