package hancho.todayDB;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class TodayDB extends PluginBase {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd");
    private String today;
    private LinkedHashMap<String, Object> db = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        this.today = DATE_FORMAT.format(System.currentTimeMillis());
        Config dbConfig = new Config(getDataFolder().getAbsolutePath() + "/" + this.today + ".yml", Config.YAML);
        this.db = (LinkedHashMap<String, Object>) dbConfig.getAll();
    }

    @Override
    public void onDisable() {
        save(false);
    }

    public void save(boolean async) {
        if (async) {
            this.getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
                @Override
                public void onRun() {
                    save(false);
                }
            });
            return;
        }
        Config dbConfig = new Config(getDataFolder().getAbsolutePath() + "/" + this.today + ".yml", Config.YAML);
        dbConfig.setAll(this.db);
        dbConfig.save();
    }

    public void checkOutdated(){
        String now = DATE_FORMAT.format(System.currentTimeMillis());
        if(!this.today.equals(now)){
            getLogger().info("db저장중..");
            this.save(false);
            this.today = now;
            this.db.clear();
        }
    }

    public LinkedHashMap<String, Object> getData(Long date){
        String stringdate = DATE_FORMAT.format(date);
        Config data = null;
        if(!this.today.equals(stringdate)) {
            data = new Config(getDataFolder().getAbsolutePath() + "/" + stringdate + ".yml", Config.YAML);
            return (LinkedHashMap<String, Object>) data.getAll();
        }else{
            return this.db;
        }
    }

    public void put(String key, Object data){
        this.checkOutdated();
        this.db.put(key, data);
    }

    public Object get(String key){
        this.checkOutdated();
        return this.db.get(key);
    }

    public Object getOrDefault(String key, Object def){
        this.checkOutdated();
        return this.db.getOrDefault(key, def);
    }

    public ArrayList getArrayList(String key){
        this.checkOutdated();
        return (ArrayList) this.db.getOrDefault(key, new ArrayList<>());
    }

    public HashSet getHashSet(String key){
        this.checkOutdated();
        return (HashSet) this.db.getOrDefault(key, new HashSet<>());
    }

    public int getInt(String key){
        this.checkOutdated();
        return (int) this.db.getOrDefault(key, 0);
    }

    public double getDouble(String key){
        this.checkOutdated();
        return (double) this.db.getOrDefault(key, 0);
    }

    public String getString(String key){
        this.checkOutdated();
        return (String) this.db.getOrDefault(key, "");
    }

    public LinkedHashMap getLinkedHashMap(String key){
        this.checkOutdated();
        return (LinkedHashMap) this.db.getOrDefault(key, new LinkedHashMap<>());
    }

    public void addCount(String key, int count){
        this.db.put(key, getInt(key) + count);
    }

    public void remove(String key){
        this.checkOutdated();
        this.db.remove(key);
    }

    public boolean containsKey(String key){
        this.checkOutdated();
        return this.db.containsKey(key);
    }

}
