<template>
    <div>
      <el-form :rules="rules" ref="loginForm" :model="loginForm" class="loginContainer">
        <h3 class="loginTitle">系统登录</h3>
        <el-form-item prop="username">
          <el-input type="text" auto-complete="false" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" auto-complete="false" v-model="loginForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input type="text" auto-complete="false" style="width: 240px;margin-right: 5px;" v-model="loginForm.code" placeholder="点击图片更换验证码"></el-input>
          <img :src="captchaUrl" @click="updateCaptcha">
        </el-form-item>
        <el-checkbox v-model="checked" class="loginRemember">记住我</el-checkbox>
        <el-button type="primary" @click="submitLogin" style="width: 100%">登录</el-button>
      </el-form>
    </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      captchaUrl: '/captcha?time='+new Date(),
      loginForm: {
        username: 'admin',
        password: '123',
        code: '',
      },
      checked: true,
      //用于验证的规则
      rules:{
        username:[{required:true, message:'请输入用户名',trigger:'blur'}],
        password:[{required:true, message:'请输入密码',trigger:'blur'}],
        code: [{required:true, message:'请输入验证吗',trigger:'blur'}],
      }
    }
  },
  methods: {
    //表单校验
    submitLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          // console.log('error submit!!');
          this.$message('请输入所有字段!!!');
          return false;
        }
      });
    },
    updateCaptcha(){
        this.captchaUrl = '/captcha?time='+new Date();
    },
  }
}
</script>

<style>
  .loginContainer{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 15px 35px 15px 35px;
    background: #fff;
    border: 1px solid cornflowerblue;
    box-shadow: 0 0 25px #cac6c6;
  }
  .loginTitle{
    margin: 0px auto 40px auto;
    text-align: center;
  }
  .loginRemember{
    text-align: left;
    margin: 0px 0px 15px 0px;
  }
  .el-alert__content{
    display: flex;
    align-items: center;
    /*padding: 10px;*/
    /*justify-content: center;*/
  }
</style>