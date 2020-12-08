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
    //root node
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
     * Remove node
     * @param data
     *
     *Delete node: find the node to be deleted from SplayTree, then rotate the node to be as the root node,
     * and then rotate the maximum node in the left subtree of the root node at this time to the
     * left child node of the root node,then the right node of the root node is regarded as the
     * right child node of the left child node of the root node,
     * and finally the root node, which is  is deleted (also Is the node to be deleted).
     */
    public void remove(T data){
        //find the node to be deleted from SplayTree
        Node del = find(data);
        if(del == null){
            return;
        }else{
            //rotate the node to be as the root node
            splay(del, this);
            //find the maximum node in the left subtree of the root node
            Node frontNodeOfRoot = frontOfNode(root.data);
            //rotate it to the left child node of the root node
            splayToRootLeft(frontNodeOfRoot, this);
            root.left.right = root.right;
            if(root.right != null){
                root.right.parent = root.left;
            }
            root = root.left;

            root.parent.left = root.parent.right = null;
            root.parent = null;
        }
    }

    //rotate the maximum node in the left subtree of the root node at this time to the
    // left child node of the root node
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
                }
            }
        }
    }
    /**
     * Changes to the tree after adding a node (rotate the newly added node as the root node)
     * @param node
     * @param tree
     */
    private void splay(Node node, SplayTree<T> tree) {//this method will only be used when root is not null
        if(node != null){
            while(node.parent != null){
                if(node == node.parent.left){
                    if(node.parent.parent == null){
                        tree.zig(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.left){  //zig-zig
                        tree.zig_zig(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.right){
                        tree.zig_zag(node);
                    }
                }else if(node == node.parent.right){
                    if(node.parent.parent == null){
                        tree.zag(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.right){  //zag-zag
                        tree.zag_zag(node);
                    }else if(node.parent.parent != null && node.parent == node.parent.parent.left){
                        tree.zag_zig(node);
                    }
                }
            }
        }
    }

    /**
     *	find the node
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
    //find the maximum node in the left subtree of the root node at this time
    public Node frontOfNode(T data){
        Node node = find(data);
        if(root == null){
            return null;
        }else{
            Node current = node;
            Node frontNode = null;
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
     * zag（as opposed to zig）
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
     * zag-zag（as opposed to zig-zig）
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
     * zig-zag
     * @param x
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
     * zag-zig（as opposed to zig-zag）
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


    public List<Node> breadthFirstSearch(){
        return cBreadthFirstSearch(root);
    }
    private List<Node> cBreadthFirstSearch(Node node) {
        List<Node> nodes = new ArrayList<Node>();
        Deque<Node> deque = new ArrayDeque<Node>();
        if(node != null){
            deque.offer(node);
        }
        while(!deque.isEmpty()){
            Node first = deque.poll();
            nodes.add(first);
            if(first.left != null){
                deque.offer(first.left);
            }
            if(first.right != null){
                deque.offer(first.right);
            }
        }
        return nodes;
    }
}
