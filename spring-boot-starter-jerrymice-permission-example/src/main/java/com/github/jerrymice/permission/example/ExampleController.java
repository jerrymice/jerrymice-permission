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

    @RequestMapping("/api/example/query")
    @ResponseBody
    @Permission("R1001")
    public Result query() {
        return new ResultInfo<>(true).setObject("查询结果");
    }

    @RequestMapping("/api/example/edit")
    @ResponseBody
    @Permission("R1002")
    public Result edit() {
        return new ResultInfo<>(true).setObject("编辑结果");
    }

    @RequestMapping("/api/example/download")
    @ResponseBody
    @Permission("R1003")
    public Result download() {
        return new ResultInfo<>(true).setObject("下载结果");
    }

    /**
     * 用户角色为管理员且用户拥有资源权限且用户名为admin的用户有权访问,或者用户名为administrator的用户有权访问
     *
     * @return
     */
    @RequestMapping("/api/example/delete")
    @ResponseBody
    @Permission("R1004")
    public Result delete() {
        return new ResultInfo<>(true).setObject("删除结果");
    }

    /**
     * 用户必须拥有资源编辑,查询,下载三个功能才能访问该接口
     *
     * @return
     */
    @RequestMapping("/api/example/share")
    @ResponseBody
    @Permission("P['R']['R1001'] && P['R']['R1002'] && P['R']['R1003']")
    public Result share() {
        return new ResultInfo<>(true).setObject("分享结果");
    }


    /**
     * 第一个注解,与第二个和第三个等价. 用户角色为管理员且用户拥有删除资源权限且用户名为admin的用户有权访问,或者用户名为administrator的用户有权访问
     * 可以根据自己的情况拆分.
     *
     * @return
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
     * 用户必须有查询资源权限,且特权类型==2,或者用户名为test的用户可以访问
     *
     * @return
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
     * 1.@PermissionMeta(var = "paramStatus",defaultValue = "E.status",eval = "paramStatus.filter(i=>i>1003)")
     * var:在JS中定义一个paramsStatus的变量名,如果未指定将定义一个status的变量名,
     * defaultValue:如果前台未传值,那么取默认值E.status
     * eval: 取paramStatus大于1003的值并赋值给status,
     * <p>
     * 2.@PermissionResult(var = "result",eval = "result.object.status.removes(i=>i>1004);result.object.data.each(i=>i.delete(['amount','ordernum']))",returnVar = "result")
     * var:在JS中定义一个变量,变量名为result,value值是当前接口的返回值
     * eval:过滤status和data
     * result: 要返回的结果变量.
     *
     * @return
     */
    @RequestMapping("/api/example/select")
    @ResponseBody
    @PermissionResult(var = "result", eval = "result.object.status.removes(i=>i>1004);result.object.data.each(i=>i.delete(['amount','ordernum']))", returnVar = "result")
    public Result select(@PermissionMeta(var = "paramStatus", defaultValue = "E.status", eval = "paramStatus.filter(i=>i>1003)") Integer[] status, @PermissionMeta(defaultValue = "P.U.admin.id") String id) {
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
     * @param engine
     * @return
     */
    @RequestMapping("/api/example/page")
    @ResponseBody
    public Result page(PermissionEngine engine,@PermissionMeta(var = "paramsStatus") Integer[] status) {
        ResultInfo<Object> result = new ResultInfo<>(true);
        /**
         * 判断用户是否有权访问该接口
         */
        boolean permission = engine.contain("R1001");
        if (permission) {
            System.out.println("第二次:"+engine);
            boolean bool = engine.bool("paramsStatus.filter(i=>i>300).length>0");

            /**
             * 验证用户的参数是否超出权限范围
             */
            if(bool){
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
            }else{
                //// TODO: 2018/9/21 用户参数超出权限范围
            }

        } else {
            //// TODO: 2018/9/21 用户没有权限访问该接口
        }
        return result;
    }

}
