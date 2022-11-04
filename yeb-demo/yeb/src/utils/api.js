import axios from "axios";
import {Message} from "element-ui";
//导入路由，用于跳转
import router from '../router'

//响应拦截器
axios.interceptors.response.use(success=>{
    //业务逻辑错误
    if(success.status && success.status==200){
        if(success.data.code==500 || success.data.code==401 || success.data.code==403){
            Message.error({message:success.data.message});
            return;
        }
        if(success.data.message){
            Message.success({message:success.data.message})
        }
        //返回json对象
        return success.data;
    }
}, error => {
    if(error.response.code==504 || error.response.code==404){
        Message.error({message:'服务器被吃了!!!'});
    }else if(error.response.code==403){
        Message.error({message:'权限不足，请联系管理员！'});
    }else if(error.response.code==401){
        Message.error({message:'尚未登录，请登录'});
        router.replace('/');
    }else{
        if(error.response.data.message){
            Message.error({message:error.response.data.message});
        }else {
            Message.error({message:'未知错误！'});
        }
    }
    //错误就不能返回消息了
    return;
})

let base = '';

//传送json格式的post请求
export const postRequest = (url, params)=>{
    return axios({
        method: 'post',
        url:  `${base}${url}`,
        data: params
    })
}

