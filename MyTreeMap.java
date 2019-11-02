//用二叉搜索树实现Map中的方法

import java.util.Set;
import java.util.TreeSet;

public class MyTreeMap {
    public static class Node{
        private int key;
        private int value;
        private Node left;
        private Node right;

        public Node(int key,int value){
            this.key=key;
            this.value=value;
        }

        public int getKey(){   //返回key值
            return key;
        }

        public int getValue(){   //返回value值
            return value;
        }
    }

    private Node root=null;  //刚开始搜索树为空树

    /*
        返回 key 对应的 value
        若原搜索树中没有key值对应的结点 ，那么假设它的value为-1
     */
    public int  get(int key){
        Node cur=root;
        while(cur!=null){
            if(key==cur.key){
                return cur.value;
            }else if(key<cur.key){
                cur=cur.left;
            }else{
                cur=cur.right;
            }
        }
        return -1;
    }

    //返回 key 对应的 value，key 不存在，返回默认值
    public  int getOrDefault(int key, int defaultValue){
        Node cur=root;
        while(cur!=null){
            if(key==cur.key){
                return cur.value;
            }else if(key<cur.key){
                cur=cur.left;
            }else{
                cur=cur.right;
            }
        }
        return defaultValue;
    }

    /*
        设置 key 对应的 value
        若原搜索树中没有key值对应的结点，那么假设它的返回值为-1
        返回值：老的value
     */
    public int put(int key, int value){
        if(root==null){  //原搜索树为空树，直接插入
            root=new Node(key,value);
            return -1;
        }
        Node parent=null;
        Node cur=root;
        while(cur!=null){
            if(key==cur.key){
                int oldValue=cur.value;
                cur.value=value;  //若原搜索树中有改映射关系   则覆盖旧值
                return oldValue;
            }else if(key<cur.key){
                parent=cur;
                cur=cur.left;
            }else{
                parent=cur;
                cur=cur.right;
            }
        }
        //若原搜索树中没有该映射关系  则插入新的结点
        Node node=new Node(key,value);
        if(cur==parent.left){
            parent.left=node;
        }else{
            parent.right=node;
        }
        return -1;
    }

    /*
        删除 key 对应的映射关系
     */
    public int  remove(int key){
        Node parent=null;
        Node cur=root;
        while(root!=null){
            if(key==cur.key){   //找到这个结点
                int oldValue=cur.value;
                removeNode(parent,cur);
                return oldValue;
            }else if(key<cur.key){
                parent=cur;
                cur=cur.left;
            }else{
                parent=cur;
                cur=cur.right;
            }
        }
        return -1;  //没有该结点  返回-1
    }

    private void removeNode(Node parent, Node cur) {
        if(cur.left==null){
            if(cur==root){
                root=cur.right;
            }else if(cur==parent.left){
                parent.left=cur.right;
            }else{
                parent.right=cur.right;
            }
        }else if(cur.right==null){
            if(cur==null){
                root=cur.left;
            }else if(cur==parent.left){
                parent.left=cur.left;
            }else{
                parent.right=cur.left;
            }
        }else{  //替罪羔羊为右子树结点值最小的结点，采用替换法
            Node goatParent=cur;
            Node goat=cur.right;
            while(goat.left!=null){
                goatParent=cur;
                goat=goat.left;
            }  //退出循环找到替罪羔羊

            cur.key=goat.key;   //替换法
            cur.value=goat.value;

            if(goat==goatParent.left){   //删除替罪羔羊
                goatParent.left=goat.right;
            }else{
                goatParent.right=goat.right;
            }
        }
    }

    /*
        返回所有 key 的不重复集合
        采用中序遍历的思想
     */
    public Set<Integer> keySet(){
        return inorderKeySet(root);
    }

    private Set<Integer> inorderKeySet(Node root) {
        Set<Integer> set=new TreeSet<>();
        if(root!=null){
            Set<Integer> left=inorderKeySet(root.left);
            Set<Integer> right=inorderKeySet(root.right);
            set.addAll(left);
            set.add(root.key);
            set.addAll(right);
         }
         return set;
    }

    //判断是否包含 key
    public boolean  containsKey(int key){
        return  get(key)!=-1;
    }

    //判断是否包含 value
    boolean containsValue(int value){
        Node cur=root;
        while(cur!=null){
            if(value==cur.value){
                return true ;
            }else if(value<cur.value){
                cur=cur.left;
            }else{
                cur=cur.right;
            }
        }
        return false;
    }
}
