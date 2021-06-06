
class Record {

    int id;
    int seconds;
    double gpsN;
    double gpsW;

    int WEST, NORTH;
    boolean visited;

    Record(int i, int min, double gpsn, double gpsw) {
        id = i;
        seconds = min;
        gpsN = gpsn;
        gpsW = gpsw;
        visited = false;
    }

    // converts distance (km) into time (seconds) using 60kmph speed
    int timeTo(Record other) {
        double d = dist(other);
        int seconds = (int) ((d / 60) * 3600);
        return seconds;
    }
    
    // finds between two GPS points
    double dist(Record other) {
        final int Radius = 6371;
        double lat1 = other.gpsN;
        double lon1 = other.gpsW;
        double lat2 = this.gpsN;
        double lon2 = this.gpsW;
        double latDistance = radians(lat2 - lat1);
        double lonDistance = radians(lon2 - lon1);
        double angle = Math.sin(latDistance / 2) * 
                Math.sin(latDistance / 2) + 
                Math.cos(radians(lat1)) * 
                Math.cos(radians(lat2)) * 
                Math.sin(lonDistance / 2) * 
                Math.sin(lonDistance / 2);
        double unit = 2 * Math.atan2(Math.sqrt(angle), Math.sqrt(1 - angle));
        return Radius * unit;
    }
    
    private double radians(double value) {
        return value * Math.PI / 180;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean f) {
        visited = f;
    }
}
