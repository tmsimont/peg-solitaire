public class Main
{
    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(board);
        Solver solver = new Solver();
        solver.solveBoard(board);
    }
}
