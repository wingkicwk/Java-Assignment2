import java.util.List;

class SplayTreeTest {
    public static void main(String[] args) {
        SplayTree<Integer> tree1 = new SplayTree<Integer>();
        for (int i = 0; i< 100; i++){
            tree1.add(i);
        }
//        tree1.add(20);
//        System.out.println( tree1.breadthFirstSearch());
//        tree1.add(18);
//        System.out.println( tree1.breadthFirstSearch());
//        tree1.add(22);
//        System.out.println( tree1.breadthFirstSearch());
//        tree1.add(24);
//        System.out.println( tree1.breadthFirstSearch());
//        tree1.add(23);
//        System.out.println( tree1.breadthFirstSearch());
//
//        tree1.remove(22);
        System.out.println( tree1.breadthFirstSearch());
        System.out.println();


    }

}
