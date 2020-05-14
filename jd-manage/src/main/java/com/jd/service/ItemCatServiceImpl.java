package com.jd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.mapper.ItemCatMapper;
import com.jd.pojo.ItemCat;
import com.jd.util.ObjectMapperUtil;
import com.jd.vo.EasyUI_Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;


import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired(required = false)
    private ItemCatMapper itemCatMapper;
    @Autowired(required = false)
    private JedisCluster jedis;

    /**
     *通过商品分类id查询出分类信息，再获取分类名称
     */
    @Override
    public String findItemCatNameById(Long itemCatId) {
        ItemCat itemCat = itemCatMapper.selectById(itemCatId);
        return itemCat.getName();
    }
//通过父id查询下级目录数据
    @Override
    public List<EasyUI_Tree> findItemCatByParentId(Long parentId) {
        List<EasyUI_Tree> list = new ArrayList<>();
        //1.获取数据库数据
        List<ItemCat> itemCatList = findItemCatList(parentId);
        //2.把获取到的数据循环，把id，name都取出来，同时判断是否是父节点，
        //来设置state状态值，然后再添加到list集合中
        for (ItemCat itemCat:itemCatList){
            Long id = itemCat.getId();   //分类id
            String name = itemCat.getName();  //分类名称
            //设置state状态值，closed关闭，open打开；根据isParent来判断
            String state = itemCat.getIsParent() ? "closed" : "open";
            EasyUI_Tree tree = new EasyUI_Tree(id, name, state);  //创建对象
            list.add(tree);  //装入集合
        }
        return list;
    }

    /**
     *通过缓存查询子分类
     */
    @Override

    public List<EasyUI_Tree> findItemCatByCache(Long parentId) {
    //1.创建一个用来转子分类的集合
        List<EasyUI_Tree> treeList = new ArrayList<>();
        String key="ITEM_CAT_"+parentId;   //key=ITEM_CAT_0
        //查询redis中数据
        String result = jedis.get(key);
        //2.查询缓存中如果有数据，将json转换成对象返回
        if (!StringUtils.isEmpty(result)){
            //把json串转换成对象，返回
            System.out.println("通过缓存查询的数据！！！");
          treeList = ObjectMapperUtil.toObject(result,treeList.getClass());
        }else {
            //3.查询缓存中没有数据，去查询mysql数据库；将对象转换成json串存入到redis中
            //调用原来的方法查询mysql数据库
            treeList=findItemCatByParentId(parentId);
            System.out.println("数据通过mysql查询！！！");
            //将对象转换成json
            String json = ObjectMapperUtil.toJSON(treeList);
            //将数据保存到redis中
            jedis.set(key,json);
        }
        //返回集合对象
        return treeList;
    }
    //1.通过父id 获取下级目录数据，这个方法是供上个方法调用
    public List<ItemCat> findItemCatList(Long parentId){
        //创建mybatis-plus查询条件构造器
        QueryWrapper<ItemCat> query = new QueryWrapper<>();
        //设置查询条件，根据父id查询
        query.eq("parent_id",parentId);
      return   itemCatMapper.selectList(query);
    }
}
