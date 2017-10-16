package info.jiuyou.jiuchi;

/**
 * ==========================================
 * <p>
 * 版   权 ：jianshijiuyou(c) 2017
 * <br/>
 * 作   者 ：wq
 * <br/>
 * 版   本 ：1.0
 * <br/>
 * 创建日期 ：2017/10/16  21:02
 * <br/>
 * 描   述 ：
 * <br/>
 * 修订历史 ：
 * </p>
 * ==========================================
 */
public class ChiItem {
    private int type;
    private float x;

    public ChiItem(int type, float x) {
        this.type = type;
        this.x = x;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getX() {

        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float offsetX(float offset){
        x-=offset;
        return x;
    }
}
