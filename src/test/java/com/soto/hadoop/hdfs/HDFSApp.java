package com.soto.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;

/**
 * Hadoop HDFS Java API 操作
 */
public class HDFSApp {

    public static final String HDFS_PATH = "hdfs://sotowang-pc:8020";

    FileSystem fileSystem = null;
    Configuration configuration = null;

    @Before
    public void setUp() throws Exception {
        System.out.println("HDFSApp.setUp");

        configuration = new Configuration();
        fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration);
    }


    @After
    public void tearDown() throws Exception {
        configuration = null;
        fileSystem = null;
        System.out.println("HDFSApp.tearDown");

    }

    /**
     * 创建HDFS目录
     *
     * @throws Exception
     */
    @Test
    public void mkdir() throws Exception {
        fileSystem.mkdirs(new Path("/hdfsapi/test"));
    }

    /**
     * 创建文件
     *
     * @throws Exception
     */
    @Test
    public void create() throws Exception {
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/a.txt"));
        output.write("hello hadoop".getBytes());
        output.flush();
        output.close();
    }

    /**
     * 查看HDFS文件上的内容
     *
     * @throws Exception
     */
    @Test
    public void cat() throws Exception {
        FSDataInputStream in = fileSystem.open(new Path("/hdfsapi/test/b.txt"));
        IOUtils.copyBytes(in, System.out, 1024);
    }

    /**
     * 重命名
     *
     * @throws Exception
     */
    @Test
    public void rename() throws Exception {
        Path oldPath = new Path("/hdfsapi/test/a.txt");

        Path newPath = new Path("/hdfsapi/test/b.txt");
        fileSystem.rename(oldPath, newPath);
    }


    /**
     * 上传文件到HDFS
     *
     * @throws Exception
     */
    @Test
    public void copyFromLocalFile() throws Exception {
        Path localPath = new Path("/home/sotowang/user/aur/hadoop/data/hello.txt");
        Path hdfsPath = new Path("/");
        fileSystem.copyFromLocalFile(localPath, hdfsPath);

    }

    /**
     * 上传文件到HDFS
     *
     * @throws Exception
     */
    @Test
    public void copyFromLocalFileWithProgress() throws Exception {
        InputStream in = new BufferedInputStream(
                new FileInputStream(
                        new File("/home/sotowang/第9章_前沿技术拓展Spark,Flink,Beam.mp4")
                ));
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/第9章.mp4"),
                new Progressable() {
                    public void progress() {
                        System.out.println("#");
                    }
                });
        IOUtils.copyBytes(in, output, 4096);
    }

    /**
     * 下载HDFS文件
     * @throws IOException
     */
    @Test
    public void copyToLocalFile() throws IOException {
        Path localPath = new Path("/home/sotowang/h.txt");
        Path hdfsPath = new Path("/hdfsapi/test/hello.txt");
        fileSystem.copyToLocalFile(hdfsPath, localPath);
    }

    /**
     * 查看某个目录下的所有文件
     */
    @Test
    public void listFiles() throws Exception {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));
        for (FileStatus fileStatus : fileStatuses) {
            String isDir = fileStatus.isDirectory() ? "文件夹" : "文件";
            short replication = fileStatus.getReplication();
            long len = fileStatus.getLen();
            String path = fileStatus.getPath().toString();


            System.out.println(isDir + "\t" + replication + "\t" + len + "\t" + path);
        }
    }

    /**
     * 删除文件
     */
    @Test
    public void delete() throws Exception {
        fileSystem.delete(new Path("/out"),true);
    }


}
