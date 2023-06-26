package com.xb.service.impl;

import com.xb.entity.User;
import com.zxb.liqi.core.table.Table;
import com.zxb.liqi.core.table.service.impl.ServiceImpl;
import com.zxb.liqi.enums.ConditionalOperator;
import com.xb.service.MainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description
 */
@Service
public class MainServiceImpl extends ServiceImpl<User> implements MainService {

    @Override
    public User getObj(User user) {
        Table<User> table = new Table<>();
//        table.from(User.class)
//                .select(User::getDmName, User::getId, User::getUsername)
//                .conditionEq(User::getDmName, "")
//                .condition(user.getId() == 0, User::getId, ConditionalOperator.GE, "")
//                .or((currentTable) -> currentTable.conditionEq(User::getDmName, "2").or().conditionEq(User::getId, 0))
//                .condition(User::getUsername, ConditionalOperator.EQ, "")
//                .like(User::getDmName, "%" + user.getUsername() + "%")
//                .in(User::getId, new ArrayList<Integer>(){{add(1);add(2);}})
//                .and((currentTable) -> currentTable.conditionEq(User::getId, 1).and().conditionEq(User::getPassword, "1233"))
//                .groupBy(User::getDmName, User::getId).orderByAsc(User::getId, User::getDmName).lastSql("limit 10")
//                .in(User::getPassword, "123", "21", "32", "111")
//                .query();
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        list.add(5);
//        list.add(6);
//        table.from(User.class).select(User::getId, User::getDmName, User::getUsername)
//                .condition(User::getPassword, ConditionalOperator.EQ, user.getPassword())
//                .in(User::getId, list)
//                .in(User::getDmName, "xiaobai", "111aaa", "yz").query();
//        System.out.println(table.getSqlStatements());
//        System.out.println(table.getOrderByAsc());

//        table.from(User.class).setUpdate(User::getDmName)
//                .conditionEq(User::getId, "")
//                .update();

//        int update = updateById(user);

        int save = save(user);

        return null;
//        return getOne(table);
    }
}
