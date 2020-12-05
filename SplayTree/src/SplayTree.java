import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class SplayTree <T extends Comparable<T>>{
    public class Node{
        T data;
        Node parent;
        Node left;
        Node right;
        public Node(T data, Node parent, Node left, Node right){
            this.data = data;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
        public String toString(){
            return "[data="+data+"]";
        }
    }
    //根节点
    private Node root;

    public SplayTree(){
        root = null;
    }
    public SplayTree(T data){
        root = new Node(data, null, null, null);
    }
    /**
     * Adding node
     * @param data
     *Add nodes: add nodes just like in binary sorting tree, but the difference is that adding nodes in splay tree,
     *       rotate the newly added node will be rotated to the root node position.
     * 添加节点：跟二叉排序树一样添加节点，不过与二叉排序树添加节点不同的是伸展树添加节点后，
     * 会把新添加的节点旋转到根节点位置。
     */
    public void add(T data){
        Node current = root;
        if(current == null){
            root = new Node(data, null, null, null);
        }
        else{
            int result = 0;
            Node parent = current;
            while(current != null){
                parent = current;
                result = data.compareTo(current.data);
                if(result > 0){
                    current = current.right;
                }else{
                    current = current.left;
                }
            }
            Node newNode = new Node(data, parent, null, null);
            if(result > 0){
                parent.right = newNode;
            }else{
                parent.left = newNode;
            }

            splay(newNode, this);
        }
    }

    /**
     * 删除节点
     * @param data
     * 删除节点：从SplayTree中找出要删除的节点，然后将该节点旋转为根节点，然后再把此时的根节点的左子树中
     * 的最大值节点(前驱)旋转为根节点的左子节点（这样根节点的左子节点的子节点只会有左子树，因为它最大），紧接着把根节点
     * 的右节点当做根节点的左子节点的右子节点，最后在 删除根节点（也就是要删除的节点）。
     */
    public void remove(T data){
        //找到要删除的节点
        Node del = find(data);
        if(del == null){
            return;
        }else{
            //把要删除的节点旋转为根节点
            splay(del, this);
            //找到此时根节点的前驱
            Node frontNodeOfRoot = frontOfNode(root.data);
            //把跟的前驱旋转为根节点的左子节点
            splayToRootLeft(frontNodeOfRoot, this);
            //
            root.left.right = root.right;
            if(root.right != null){
                root.right.parent = root.left;
            }
            root = root.left;

            root.parent.left = root.parent.right = null;
            root.parent = null;
        }
    }

    //把根的前驱旋转为根节点的左子节点
    private void splayToRootLeft(Node node, SplayTree<T> tree) {
        if(node != null){
            while(node.parent != root && node.parent != null){
                if(node == node.parent.left){
                    if(node.parent.parent == null){
                        tree.zig(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.left){ //zig-zig
                        tree.zig_zig(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.right){
                        tree.zig_zag(node);
                    }
                }else if(node == node.parent.right){
                    if(node.parent.parent == null){
                        tree.zag(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.right){ //zag-zag
                        tree.zag_zag(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.left){
                        tree.zag_zig(node);
                    }
                }else{
                    //nikansha?
                }
            }
        }
    }
    /**
     * 添加节点后对树的改变（将新添加的节点旋转为根节点）
     * @param node
     * @param tree
     */
    private void splay(Node node, SplayTree<T> tree) {//首先要明确一点，该方法只会在根不为空的判断里面出现
        if(node != null){
            while(node.parent != null){
                if(node == node.parent.left){
                    if(node.parent.parent == null){
                        tree.zig(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.left){ //zig-zig
                        tree.zig_zig(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.right){
                        tree.zig_zag(node);
                    }
                }else if(node == node.parent.right){
                    if(node.parent.parent == null){
                        tree.zag(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.right){ //zag-zag
                        tree.zag_zag(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.left){
                        tree.zag_zig(node);
                    }
                }else{
                    //nikansha?
                }
            }
        }
    }

    /**
     *	搜索节点
     * @param data
     * @return
     */
    private Node find(T data){
        Node current = root;
        if(current == null){
            return null;
        }else{
            int result = 0;
            while(current != null){
                result = data.compareTo(current.data);
                if(result > 0){
                    current = current.right;
                }else if(result < 0){
                    current = current.left;
                }else{
                    return current;
                }
            }
            return null;
        }
    }
    //找到某节点的前驱
    public Node frontOfNode(T data){
        Node node = find(data);
        if(root == null){
            return null;
        }else{
            Node current = node;
            Node frontNode = null;  //前驱节点
            int result = 0;
            while(current != null){
                result = node.data.compareTo(current.data);
                if(result > 0){
                    frontNode = current;
                    current = current.right;
                }else if(result < 0){
                    current = current.left;
                }else{
                    return current;
                }
            }
            return frontNode;
        }
    }
    /**
     * zig
     * @param node
     * 		root->  y			root->	x
     * 				││					││
     * 			x───┘└   ->			────┘└──y
     * 			││							││
     * 		────┘└						────┘└
     */
    private void zig(Node x){
        Node y = x.parent;
        x.parent = y.parent;
        root = x;
        y.left = x.right;
        if(x.right != null){
            x.right.parent = y;
        }
        x.right = y;
        y.parent = x;
    }
    /**
     * zag（与zig相反）
     * @param node
     */
    private void zag(Node x){
        Node y = x.parent;
        x.parent = y.parent;
        root = x;
        y.right = x.left;
        if(x.left != null){
            x.left.parent = y;
        }
        x.left = y;
        y.parent = x;
    }
    /**
     * zig-zig
     * @param x
     * 				│		│
     * 				z		x
     * 				││		││
     * 			y───┘└ 	-》	┘└──y
     * 			││				││
     * 		x───┘└ 				┘└─z
     * 	───┘└ 					───┘└
     *
     */
    private void zig_zig(Node x){
        Node y = x.parent;
        Node z = y.parent;

        x.parent = z.parent;
        if (z.parent != null) {
            if(z.parent.left == z){
                z.parent.left = x;
            }else{
                z.parent.right = x;
            }
        }

        y.left = x.right;
        if(x.right != null){
            x.right.parent = y;
        }
        z.left = y.right;
        if(y.right != null){
            y.right.parent = z;
        }

        y.right = z;
        z.parent = y;

        x.right = y;
        y.parent = x;

        if(x.parent == null){
            root = x;
        }
    }
    /**
     * zag-zag（与zig-zig相反）
     * @param x
     */
    private void zag_zag(Node x){
        Node y = x.parent;
        Node z = y.parent;

        x.parent = z.parent;
        if (z.parent != null) {
            if(z.parent.left == z){
                z.parent.left = x;
            }else{
                z.parent.right = x;
            }
        }

        y.right = x.left;
        if(x.left != null){
            x.left.parent = y;
        }
        z.right = y.left;
        if(y.left != null){
            y.left.parent = z;
        }

        y.left = z;
        z.parent = y;

        x.left = y;
        y.parent = x;

        if(x.parent == null){
            root = x;
        }
    }
    /**
     * zig-zag（跟AVL树中的双向左旋一样？）
     * @param x
     * 		│					│
     * 		z					x
     * 		││					││
     * 		┘└──y	-》		z───┘└──y
     * 			││			┘└─		┘└─
     * 		x───┘└─
     * 		┘└─
     */
    private void zig_zag(Node x){
        Node y = x.parent;
        Node z = y.parent;

        x.parent = z.parent;
        if(z.parent != null){
            if(z == z.parent.left){
                z.parent.left = x;
            }else{
                z.parent.right = x;
            }
        }

        z.right = x.left;
        if(x.left != null){
            x.left.parent = z;
        }
        y.left = x.right;
        if(x.right != null){
            x.right.parent = y;
        }

        x.left = z;
        z.parent = x;
        x.right = y;
        y.parent = x;

        if(x.parent == null){
            root = x;
        }
    }
    /**
     * zag-zig（跟zig-zag相反）
     * @param x
     */
    private void zag_zig(Node x){
        Node y = x.parent;
        Node z = y.parent;

        x.parent = z.parent;
        if(z.parent != null){
            if(z == z.parent.left){
                z.parent.left = x;
            }else{
                z.parent.right = x;
            }
        }

        z.left = x.right;
        if(x.right != null){
            x.right.parent = z;
        }
        y.right = x.left;
        if(x.left != null){
            x.left.parent = y;
        }

        x.right = z;
        z.parent = x;
        x.left = y;
        y.parent = x;

        if(x.parent == null){
            root = x;
        }
    }
}
