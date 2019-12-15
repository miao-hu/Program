import java.io.*;
import java.util.Comparator;

public class Scanner {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        //定义一个根结点
        TreeNode root=new TreeNode();
        //根结点的file属性设置为一个文件夹名称
        root.file=new File("E:\\大三作业\\软件工程作业");

        //扫描完后建立了一个树
        scannerDirectory(root);

        //将扫描的结果输出到  扫描结果.txt  文件中
        PrintStream out=new PrintStream(
                new FileOutputStream("扫描结果.txt"),  //文件输出流
                true,
                "UTF-8");

        //定义一个比较器  降序排列
        Comparator<TreeNode> sortByLengthDesc=new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode o1, TreeNode o2) {
                if(o1.totalLength>o2.totalLength){
                    return -1;
                }else if(o1.totalLength==o2.totalLength){
                    return 0;
                }else{
                    return 1;
                }
            }
        };

        //打印
        report(root,0,sortByLengthDesc,out);
        //关闭输出流
        out.close();
    }

    //功能：打印    （根结点，0，比较器，输出流）
    private static void report(TreeNode root, int level, Comparator<TreeNode> sortBy, PrintStream out) {
        for(int i=0;i<level*4;i++){
            out.print(" ");   // 打印空格
        }
        if(level==0){  //根结点
            out.print(root.file.getAbsolutePath());//如果是第0层，往文件中写它的绝对路径名
        }else{
            out.print(root.file.getName());  //往文件中写它的相对路径名
        }
        if(root.file.isDirectory()){   //如果该文件是个文件夹  在他的路径后面加上********
            out.print("*******");
        }
        String unit="字节";
        double length=root.totalLength;
        if (length > 1024 * 1024 * 1024) {
            unit = "G字节";   // 1G = 2^30 K
            length = length / 1024 / 1024 / 1024;
        } else if (length > 1024 * 1024) {
            unit = "M字节";   // 1M = 2^20 K
            length = length / 1024 / 1024;
        } else if (length > 1024) {
            unit = "K字节";
            length = length / 1024;
        }
        out.printf("     %.2f%s%n",length,unit);
        root.children.sort(sortBy);   //对孩子文件进行降序排序
        for(TreeNode node:root.children){   //遍历每一个子文件，也就是孩子文件
            report(node,level+1,sortBy,out);
        }
    }

    //功能：扫描文件夹下的文件
    private static void scannerDirectory(TreeNode node) {
        File[] files=node.file.listFiles();  //列举当前文件夹下的所有文件，并且存到files中
        if(files==null){
            return;
        }
        //遍历当前文件夹下的所有文件，从第一个开始遍历
        for(File file:files){
            TreeNode child=new TreeNode();
            child.file=file;
            if(file.isDirectory()){ //若当前文件是一个目录，也就是文件夹，继续对该文件夹进行扫描（深度优先遍历）
                scannerDirectory(child);
            }else{  //若当前文件不是文件夹
                child.totalLength=file.length();
            }
            node.totalLength+=child.totalLength; //上层文件夹的长度+=子文件夹的长度
            node.children.add(child);
        }
    }
}
