import java.io.File;
import java.util.ArrayList;
import java.util.List;
/*
把文件的存储当做树来看
文件夹A： 1.子文件夹B  -->子子文件夹C
		  2.abc.txt
		  3.xyz.txt

		  把每个 文件夹/文件 定义为一个结点
*/
public class TreeNode {
    File file;
    long totalLength;   // 汇总了该文件夹下所有文件的总大小
    List<TreeNode> children = new ArrayList<>();
}
