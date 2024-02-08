package io.github.ompc.erniebot4j.embedding;

public record Embedding(int index, float[] vectors) implements Comparable<Embedding> {

    @Override
    public int compareTo(Embedding o) {
        return index - o.index;
    }

}
