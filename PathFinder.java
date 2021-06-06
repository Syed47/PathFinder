import java.util.ArrayList;

class PathFinder {

    private Record[] records;
    private ArrayList<Record> path;

    PathFinder(String data) {
        Record apachePizza = new Record(0, 0, 53.38197, -6.59274);
        this.records = parseData(data);
        this.path = new ArrayList<>();
        for (Record r : records) {
            this.path.add(r);
        }
        this.path = nearestNeighbours(apachePizza);
        System.out.println("NN = " + totalTime(this.path));
        this.path = twoOpt(this.path);
        System.out.println("2-opt = " + totalTime(this.path));
    }

    Record[] parseData(String data) {
        String[] tokens = data.trim().split("\n");
        Record[] local = new Record[tokens.length];
        for (String token : tokens) {
            String[] fields = token.split(",");
            int id = Integer.parseInt(fields[0]);
            int seconds = Integer.parseInt(fields[2]) * 60;// seconds
            double gpsN = Double.parseDouble(fields[3]);
            double gpsW = Double.parseDouble(fields[4]);
            local[id - 1] = new Record(id, seconds, gpsN, gpsW);
        }
        return local;
    }

    Record[] getPath() {
        Record[] spath = new Record[path.size()];
        for (int i = 0; i < path.size(); i++) {
            spath[i] = path.get(i);
        }
        return spath;
    }

    private ArrayList<Record> nearestNeighbours(Record start) {
        ArrayList<Record> allNearestNeighbors = new ArrayList<>();
        start.setVisited(true);
        allNearestNeighbors.add(start);
        Record next = start, nearest = null;
        int visited = 0, all = records.length;
        while (visited < all) {
            int shortest = Integer.MAX_VALUE;
            for (int i = 0; i < records.length; i++) {
                if (!records[i].isVisited()) {
                    int time = next.timeTo(records[i]);
                    if (time < shortest) {
                        shortest = time;
                        nearest = records[i];
                    }
                }
            }
            nearest.setVisited(true);
            allNearestNeighbors.add(nearest);
            visited++;
            next = nearest;
        }
        return allNearestNeighbors;
    }

    private ArrayList<Record> twoOpt(ArrayList<Record> path) {
        int N = path.size();
        ArrayList<Record> best = new ArrayList<>();
        for (Record r : path) {
            best.add(r);
        }
        int maxIterations = 0;
        while (maxIterations < N / 5) {
            for (int i = 1; i < N - 2; i++) {
                for (int j = i + 1; j < N + 1; j++) {
                    if (j - i == 1)
                        continue;
                    ArrayList<Record> newPath = new ArrayList<>();
                    for (int k = 0; k < i; k++) {
                        newPath.add(best.get(k));
                    }
                    for (int k = j - 1; k >= i; k--) {
                        newPath.add(best.get(k));
                    }
                    for (int k = j; k < N; k++) {
                        newPath.add(best.get(k));
                    }
                    if (totalTime(newPath) < totalTime(best)) {
                        best = newPath;
                    }
                }
            }
            maxIterations++;
        }
        return best;
    }

    private int totalTime(ArrayList<Record> path) {
        int c = 0;
        for (int i = 1; i < path.size(); i++) {
            c += path.get(i - 1).timeTo(path.get(i));
        }
        return c;
    }
}