import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;



public class CheckmateDetector {
    private Board b;
    private LinkedList<Piece> wPieces;
    private LinkedList<Piece> bPieces;
    private LinkedList<Square> movableSquares;
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> wMoves;
    private HashMap<Square,List<Piece>> bMoves;

    /*
     - b la ban co dang choi
     - wPieces la quan co trang
     - bPieces la quan co den
     - wk Piece la vua trang
     - bk Piece la vua den
     */
    public CheckmateDetector(Board b, LinkedList<Piece> wPieces,
                             LinkedList<Piece> bPieces, King wk, King bk) {
        this.b = b;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;

        // Khoi tao gia tri tren ban co
        squares = new LinkedList<Square>();
        movableSquares = new LinkedList<Square>();
        wMoves = new HashMap<Square,List<Piece>>();
        bMoves = new HashMap<Square,List<Piece>>();

        Square[][] brd = b.getSquareArray();

        // Dat cac quan co vao trong bang bam
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares.add(brd[y][x]);
                wMoves.put(brd[y][x], new LinkedList<Piece>());
                bMoves.put(brd[y][x], new LinkedList<Piece>());
            }
        }

        // update ban co
        update();
    }

    // Cap nhat tinh hinh van co
    public void update() {
        Iterator<Piece> wIter = wPieces.iterator();
        Iterator<Piece> bIter = bPieces.iterator();

        // xoa cac nuoc di cu truoc khi update
        for (List<Piece> pieces : wMoves.values()) {
            pieces.removeAll(pieces);
        }

        for (List<Piece> pieces : bMoves.values()) {
            pieces.removeAll(pieces);
        }

        movableSquares.removeAll(movableSquares);

        // Them cac nuoc di cho quan co
        while (wIter.hasNext()) {
            Piece p = wIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getPosition() == null) {
                    wIter.remove();
                    continue;
                }

                List<Square> mvs = p.getLegalMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = wMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }

        while (bIter.hasNext()) {
            Piece p = bIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getPosition() == null) {
                    wIter.remove();
                    continue;
                }

                List<Square> mvs = p.getLegalMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = bMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }
    }

    // Kiem tra xem vua den co dang bi chieu khong?
    public boolean blackInCheck() {
        update();
        Square sq = bk.getPosition();
        if (wMoves.get(sq).isEmpty()) {
            movableSquares.addAll(squares);
            return false;
        } else return true;
    }

    // Kiem tra xem vua trang co dang bi chieu khong?
    public boolean whiteInCheck() {
        update();
        Square sq = wk.getPosition();
        if (bMoves.get(sq).isEmpty()) {
            movableSquares.addAll(squares);
            return false;
        } else return true;
    }

    //Kiem tra xem vua den da bi chieu het chua
    public boolean blackCheckMated() {
        boolean checkmate = true;
        // Kiem tra xem vua den co dang bi chieu khong?
        if (!this.blackInCheck()) return false;

        // Neu co thi vua den co the chay hay khong?
        if (canEvade(wMoves, bk)) checkmate = false;

        // Neu khong chay duoc thi co the ăn duoc quan dang chieu khong?
        List<Piece> threats = wMoves.get(bk.getPosition());
        if (canCapture(bMoves, threats, bk)) checkmate = false;

        // Neu khong duoc thi co the chặn duoc khong?
        if (canBlock(threats, bMoves, bk)) checkmate = false;

        // Neu khong thi dung la còn cái nịt!!
        return checkmate;
    }

    // Tuong tu voi vua trắng
        public boolean whiteCheckMated() {
        boolean checkmate = true;
        if (!this.whiteInCheck()) return false;
        if (canEvade(bMoves, wk)) checkmate = false;
        List<Piece> threats = bMoves.get(wk.getPosition());
        if (canCapture(wMoves, threats, wk)) checkmate = false;
        if (canBlock(threats, wMoves, wk)) checkmate = false;
        return checkmate;
    }


     // Cac ham de kiem tra co chay duoc khong?
    private boolean canEvade(Map<Square,List<Piece>> tMoves, King tKing) {
        boolean evade = false;
        List<Square> kingsMoves = tKing.getLegalMoves(b);
        Iterator<Square> iterator = kingsMoves.iterator();

        // Neu vua khong co the chay den o khac -> co the chay duoc
        while (iterator.hasNext()) {
            Square sq = iterator.next();
            if (!testMove(tKing, sq)) continue;
            if (tMoves.get(sq)==null) {
                movableSquares.add(sq);
                evade = true;
            }
        }

        return evade;
    }

    // Ham kiem tra co an duoc khong?
    private boolean canCapture(Map<Square,List<Piece>> poss,
                               List<Piece> threats, King k) {

        boolean capture = false;
        if (threats.size() == 1) {
            Square sq = threats.get(0).getPosition();
        //Nếu quân cờ đang chiếu tướng mà tự vua có thể ăn được -> có thể ăn
            if (k.getLegalMoves(b).contains(sq)) {
                movableSquares.add(sq);
                if (testMove(k, sq)) {
                    capture = true;
                }
            }
        // Thêm vào danh sách những quân ăn được quân đang chiếu tướng
            List<Piece> caps = poss.get(sq);
            ConcurrentLinkedDeque<Piece> capturers = new ConcurrentLinkedDeque<Piece>();
            capturers.addAll(caps);

            if (!capturers.isEmpty()) {
                movableSquares.add(sq);
                for (Piece p : capturers) {
                    if (testMove(p, sq)) {
                        capture = true;
                    }
                }
            }
        }

        return capture;
    }

    // Ham kiem tra co the can duoc khong?
    private boolean canBlock(List<Piece> threats,
                             Map <Square,List<Piece>> blockMoves, King k) {
        boolean blockable = false;
        //Lay ra o cua quan chieu tuong, vua, ban co
        if (threats.size() == 1) {
            Square ts = threats.get(0).getPosition();
            Square ks = k.getPosition();
            Square[][] brdArray = b.getSquareArray();
        //Cung 1 cột, tìm đường chiếu
            if (ks.getXNum() == ts.getXNum()) {
                int max = Math.max(ks.getYNum(), ts.getYNum());
                int min = Math.min(ks.getYNum(), ts.getYNum());

                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks = blockMoves.get(brdArray[i][ks.getXNum()]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);

                    if (!blockers.isEmpty()) {
                        movableSquares.add(brdArray[i][ks.getXNum()]);
                        for (Piece p : blockers) {
                            if (testMove(p,brdArray[i][ks.getXNum()])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }
        // Neu cung hang, tim duong chieu
            if (ks.getYNum() == ts.getYNum()) {
                int max = Math.max(ks.getXNum(), ts.getXNum());
                int min = Math.min(ks.getXNum(), ts.getXNum());

                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks =
                            blockMoves.get(brdArray[ks.getYNum()][i]);
                    ConcurrentLinkedDeque<Piece> blockers =
                            new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);

                    if (!blockers.isEmpty()) {

                        movableSquares.add(brdArray[ks.getYNum()][i]);

                        for (Piece p : blockers) {
                            if (testMove(p, brdArray[ks.getYNum()][i])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }
        // Chiếu theo đường chéo
            Class<? extends Piece> tC = threats.get(0).getClass();

            if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
                int kX = ks.getXNum();
                int kY = ks.getYNum();
                int tX = ts.getXNum();
                int tY = ts.getYNum();
            // TH1: vua ở bên phải trên của quân chiếu
                if (kX > tX && kY > tY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY++;
                        List<Piece> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
            //TH2: vua ở bên phải dưới của quân chiếu
                if (kX > tX && tY > kY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY--;
                        List<Piece> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
            //TH3: trái trên
                if (tX > kX && kY > tY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY++;
                        List<Piece> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers =
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
            //TH4: trái dưới
                if (tX > kX && tY > kY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY--;
                        List<Piece> blks =
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers =
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return blockable;
    }

    /*Hàm chứa toàn bộ ô có thể đi.
    Mặc định là tất cả các ô, sẽ giảm nếu bị chiếu một số ô
    Biến b là biến hiển thị có phải lượt quân trắng hay không*/
    public List<Square> getAllowableSquares(boolean b) {
        movableSquares.removeAll(movableSquares);
        if (whiteInCheck()) {
            whiteCheckMated();
        } else if (blackInCheck()) {
            blackCheckMated();
        }
        return movableSquares;
    }

    /* Kiểm tra xem nước đi có bị chiếu không?
    Biến p là quân cờ
    Biến sq là ô định đi tới
    Trả false nếu đi sẽ bị chiếu*/
    public boolean testMove(Piece p, Square sq) {
        Piece c = sq.getOccupyingPiece();

        boolean movetest = true;
        Square init = p.getPosition();

        p.move(sq);
        update();

        if (p.getColor() == 0 && blackInCheck()) movetest = false;
        else if (p.getColor() == 1 && whiteInCheck()) movetest = false;

        p.move(init);
        if (c != null) sq.put(c);

        update();

        movableSquares.addAll(squares);
        return movetest;
    }

}