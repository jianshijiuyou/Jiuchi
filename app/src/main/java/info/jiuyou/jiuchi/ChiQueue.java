package info.jiuyou.jiuchi;

import java.util.LinkedList;
import java.util.List;

/**
 * ==========================================
 * <p>
 * 版   权 ：jianshijiuyou(c) 2017
 * <br/>
 * 作   者 ：wq
 * <br/>
 * 版   本 ：1.0
 * <br/>
 * 创建日期 ：2017/10/16  20:27
 * <br/>
 * 描   述 ：
 * <br/>
 * 修订历史 ：
 * </p>
 * ==========================================
 */
public class ChiQueue {

    private LinkedList<ChiItem> list;
    private int length;

    public ChiQueue(int length) {
        this.length = length;
    }


    public LinkedList<ChiItem> getList() {


        return list;
    }



}
