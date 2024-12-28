public class Pessoa {
    private String name;
    private LevelType level;
    private int id;


    // Enum para representar diferentes ID
    public enum LevelType {
        JUNIOR(0, "JUNIOR"),
        PLENO(1, "PLENO"),
        SENIOR(2, "SENIOR");

        private int level;
        private final String description;

        LevelType(int level, String description) {
            this.level = level;
            this.description = description;
        }

        public int getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }

        public static LevelType getByLevel(int level) {
            for (LevelType type : values()) {
                if (type.getLevel() == level) {
                    return type;
                }
            }
            return null;
        }
    }


    public Pessoa(String name, int level, int id){
        this.name = name;
        this.level = LevelType.getByLevel(level);
        this.id = id;
    }

    public Pessoa(String name, int level){
        this.name = name;
        this.level = LevelType.getByLevel(level);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LevelType getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = LevelType.getByLevel(level);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
