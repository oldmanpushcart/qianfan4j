package io.github.ompc.erniebot4j.cloud.baidu;

/**
 * BCE区域
 * <p>
 *
 * @param code 区域编码
 * @see <a href="https://cloud.baidu.com/doc/BOS/s/8k3bcx5zv">BOS区域编码</a>
 */
public record BceRegion(String code) {

    @Override
    public String toString() {
        return code;
    }

    /**
     * 北京
     */
    public static final BceRegion BJ = new BceRegion("bj");

    /**
     * 广州
     */
    public static final BceRegion GZ = new BceRegion("gz");

    /**
     * 上海
     */
    public static final BceRegion SU = new BceRegion("su");

    /**
     * 香港
     */
    public static final BceRegion BD = new BceRegion("bd");

    /**
     * 成都
     */
    public static final BceRegion CD = new BceRegion("cd");

}
