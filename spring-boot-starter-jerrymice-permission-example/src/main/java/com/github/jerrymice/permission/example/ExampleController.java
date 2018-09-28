package com.github.jerrymice.permission.example;

import com.github.jerrymice.common.entity.entity.Result;
import com.github.jerrymice.common.entity.entity.ResultInfo;
import com.github.jerrymice.permission.annotation.Permission;
import com.github.jerrymice.permission.annotation.PermissionMeta;
import com.github.jerrymice.permission.annotation.PermissionResult;
import com.github.jerrymice.permission.engine.PermissionEngine;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author tumingjian
 *         说明:Permission权限注解的value支持字符串和JavaScript两种方式
 *         1.普通文本,根据code和name查找,根据name查找需要强制加":"号
 *         直接查找用户资源列表中的是否存在该value值的项,存在返回true,否则返回false
 *         <p>
 *         如果配置spring.jerrymice.permission.mixtureSearch为true(默认为false)
 *         那么将依次查询 资源列表,角色列表.用户名是否存在或等于该value,存在返回true,否则返回false
 *         2.javascript表达式中的特殊变量
 *         P['U'] 用户信息
 *         P['R'] 用户资源
 *         P['C'] 用户角色
 *         扩展的数据以保存在JS变量E下
 */
@Controller
public class ExampleController {
    /**
     * 登录
     *
     * @param session  当前用户session
     * @param username 用户名
     * @param password 密码
     * @return 结果集
     */
    @RequestMapping("/api/example/login")
    @ResponseBody
    public Result login(HttpSession session, String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setId(String.valueOf(System.currentTimeMillis()));
        session.setAttribute("currentUser", user);
        ResultInfo result = new ResultInfo(true).setMessage("呵呵");
        return result;
    }

    /**
     * 用户必须拥有编号为R1001的查询资源权限,才能访问该接口
     *
     * @return 结果集
     */
    @RequestMapping("/api/example/query")
    @ResponseBody
    @Permission("R1001")
    public Result query() {
        return new ResultInfo<>(true).setObject("查询结果");
    }

    /**
     * 用户必须拥有编号为R1002的编辑资源权限,才能访问该接口
     *
     * @return 结果集
     */
    @RequestMapping("/api/example/edit")
    @ResponseBody
    @Permission("R1002")
    public Result edit() {
        return new ResultInfo<>(true).setObject("编辑结果");
    }

    /**
     * 用户必须拥有编号为R1003的下载资源权限,才能访问该接口
     *
     * @return 结果集
     */
    @RequestMapping("/api/example/download")
    @ResponseBody
    @Permission("R1003")
    public Result download() {
        return new ResultInfo<>(true).setObject("下载结果");
    }

    /**
     * 用户必须拥有编号为R1004的删除资源权限,才能访问该接口
     *
     * @return 结果集
     */
    @RequestMapping("/api/example/delete")
    @ResponseBody
    @Permission("R1004")
    public Result delete() {
        return new ResultInfo<>(true).setObject("删除结果");
    }

    /**
     * 用户必须拥有资源查询,编辑,下载三个功能才能访问该接口
     *
     * @return 结果集
     */
    @RequestMapping("/api/example/share")
    @ResponseBody
    @Permission("P['R']['R1001'] && P['R']['R1002'] && P['R']['R1003']")
    public Result share() {
        return new ResultInfo<>(true).setObject("分享结果");
    }


    /**
     * 或条件 可以拆分为多个注解.
     * 第一个注解,与(第二个和第三个等价). 用户角色为管理员且用户拥有删除资源权限且用户名为admin的用户有权访问,或者用户名为administrator的用户有权访问
     * 可以根据自己的情况拆分.
     *
     * @return 结果集
     */
    @RequestMapping("/api/example/deleteAll")
    @ResponseBody
    @Permission("(P['C']['C1001'] && P['R']['R1004'] && P['U']['admin']) || P['U']['administrator']")
    /**
     * 下面两个注解与第一个注解是等价的.多个Permission表示关系或
     */
    @Permission("(P['C']['C1001'] && P['R']['1004'] && P['U']['admin'])")
    @Permission("P['U']['administrator']")
    public Result deleteAll() {
        return new ResultInfo<>(true).setObject("删除结果");
    }


    /**
     * 或条件 可以拆分为多个注解
     * 用户必须有查询资源权限,且特权类型==2,或者用户名为test的用户可以访问
     *
     * @return 结果集
     */
    @RequestMapping("/api/example/vip")
    @ResponseBody
    @Permission("P['R']['R1001'] && E['特权类型']==2")
    @Permission("P['U']['test']")
    public Result vip() {
        return new ResultInfo<>(true).setObject("审核+特权+查看");
    }

    /**
     * 更高级的参数控制和结果集控制
     *
     * @param ab     测试参数1
     * @param status 可查询的状态列表值
     *               1.@PermissionMeta(defaultValue = "E.status",eval = "status.intersect(E['status'])")
     *               属性var:在JS中定义一个变量名,如果未指定将定义一个与java方法参数名相同的JS变量名(这里为status),
     *               属性defaultValue:如果前台传入的值为Null,那么取默认值E.status
     *               eval: 取E.status与status两个数组的交集.作为参数结果.可以理解为过滤掉status参数中权限不足的数组元素.
     *               属性resultVar: 要返回的结果变量.如果resultVar的值为空,那么默认为变量名java方法的参数名(这里为status)
     *               <p>
     * @param id     当前用户ID
     * @param ddd    测试参数2
     * @return 结果集
     * 2.@PermissionResult(eval = "result.object.data.each(i=&gt;i.delete(E.selectColumn))")
     * 属性var:在JS中定义一个变量,value值是当前接口的返回值,如果var的值为空,那么默认为变量名为result
     * 属性eval:在result.object.data中删除E.selectColumn数组元素中存在的列.
     * 属性resultVar: 要返回的结果变量.如果resultVar的值为空,那么默认为变量名result
     */
    @RequestMapping("/api/example/select")
    @ResponseBody
    @Permission("R1001")
    @PermissionResult(eval = "result.object.data.each(i=>i.delete(E.selectColumn))")
    public Result select(String ab, @PermissionMeta(defaultValue = "E.status", eval = "status.intersect(E['status'])") Integer[] status, @PermissionMeta(defaultValue = "P.U.admin.id") String id, String ddd) {
        ResultInfo<Object> result = new ResultInfo<>(true);
        HashMap<String, Object> map = new HashMap<>(1);
        List<QueryResult> data = new ArrayList<>();
        data.add(new QueryResult().setId("1").setAmount("100").setOrdernum("num1").setStatus("100"));
        data.add(new QueryResult().setId("2").setAmount("200").setOrdernum("num2").setStatus("200"));
        data.add(new QueryResult().setId("3").setAmount("300").setOrdernum("num3").setStatus("300"));
        data.add(new QueryResult().setId("4").setAmount("400").setOrdernum("num4").setStatus("400"));
        data.add(new QueryResult().setId("5").setAmount("500").setOrdernum("num5").setStatus("500"));
        data.add(new QueryResult().setId("6").setAmount("600").setOrdernum("num6").setStatus("600"));
        data.add(new QueryResult().setId("7").setAmount("700").setOrdernum("num7").setStatus("700"));
        map.put("data", data);
        map.put("status", status);
        result.setObject(map);
        return result;
    }

    /**
     * API操作
     *
     * @param engine 当前用户所拥有的权限引擎.由spring mvc 自动注入当前用户所属的permission engine
     * @param status 用户可以有权限访问的状态列表.此处@PermissionMeta的作用是把这个值定义为一个JS变量
     * @return 结果集
     */
    @RequestMapping("/api/example/page")
    @ResponseBody
    public Result page(PermissionEngine engine, @PermissionMeta(defaultValue = "E.status") Integer[] status) {
        ResultInfo<Object> result = new ResultInfo<>(true);
        /**
         * 判断用户是否有权访问该接口
         */
        boolean permission = engine.contain("R1001");
        if (permission) {
            System.out.println("第二次:" + engine);
            /**
             * 用JS表达式验证用户的参数是否超出权限范围
             */
            boolean bool = engine.bool("status.filter(i=>i>300).length>0");
            if (bool) {
                HashMap<String, Object> map = new HashMap<>(1);
                List<QueryResult> data = new ArrayList<>();
                data.add(new QueryResult().setId("10").setAmount("100").setOrdernum("num1").setStatus("100"));
                data.add(new QueryResult().setId("20").setAmount("200").setOrdernum("num2").setStatus("200"));
                data.add(new QueryResult().setId("30").setAmount("300").setOrdernum("num3").setStatus("300"));
                data.add(new QueryResult().setId("40").setAmount("400").setOrdernum("num4").setStatus("400"));
                data.add(new QueryResult().setId("50").setAmount("500").setOrdernum("num5").setStatus("500"));
                data.add(new QueryResult().setId("60").setAmount("600").setOrdernum("num6").setStatus("600"));
                data.add(new QueryResult().setId("70").setAmount("700").setOrdernum("num7").setStatus("700"));
                engine.put("data", data);
                /**
                 * 结果集属性字段过滤
                 */
                Object eval = engine.eval("data.each(i=>i.delete(['amount','ordernum']))");
                map.put("data", eval);
                result.setObject(map);
            } else {
                //// TODO: 2018/9/21 用户参数超出权限范围
            }

        } else {
            //// TODO: 2018/9/21 用户没有权限访问该接口
        }
        return result;
    }

}
