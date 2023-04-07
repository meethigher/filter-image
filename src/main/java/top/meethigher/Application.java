package top.meethigher;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import top.meethigher.filter.ImageFilterUtils;

/**
 * 启动类
 *
 * @author chenchuancheng
 * @since 2023/4/7 15:43
 */
public class Application {


    @Parameter(names = "-d", description = "文件夹路径, 默认是当前jar所在路径")
    private String dir = System.getProperty("user.dir");


    @Parameter(names = "-t", description = "查询的文件类型")
    private String type = "png,jpg,jpeg";


    @Parameter(names = "-l", description = "超过指定大小的将会被查询出来, 单位B/KB/MB, 不区分大小写")
    private String length = "500KB";

    @Parameter(names = "--open", description = "是否自动打开")
    private boolean open = false;


    @Parameter(names = "--help", help = true)
    private boolean help;


    public static void main(String... args) throws Exception {
        Application application = new Application();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(application)
                .build();
        jCommander.parse(args);
        if (application.help) {
            jCommander.usage();
            return;
        }
        application.run();
    }

    private void run() throws Exception {
        ImageFilterUtils.filter(this.dir, this.type, this.length, this.open);
    }
}
