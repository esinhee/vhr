<template>
  <div class="admin-main">
    <el-row :gutter="15">
      <el-col :span="8">
        <div class="user-basic-info">
          <el-card class="box-card pannel-box" v-if="user">
            <div class="box-body">
              <div class="logo-pannel">
                <el-row :gutter="10">
                  <el-col :span="8">
                    <div class="logo">
                      <img v-if="user.userface!=''" :src="user.userface"/>
                    </div>
                  </el-col>
                  <el-col :span="16">
                    <div class="name">
                      <h3> {{user.name}}</h3>
                      <!--<span>{{loginState.userInfo.group.name}}</span>-->
                    </div>
                  </el-col>
                </el-row>
              </div>
              <div class="info-pannel">
                <ul>
                  <li><label>上次登录时间：</label></li>
                  <li><label>上次登录IP：</label></li>
                </ul>
              </div>
              <div style="clear:both;"></div>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="16">
        <div class="data-statistics">
          <el-row :gutter="20">
            <!--<el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
              <StaticPannel icon="fa-user" :num="basicInfo.adminUserCount" type="primary" text="管理员总数"/>
            </el-col>-->
            <el-col :span="8">
              <StaticPannel icon="fa-users" :num="todaytk" type="success" text="今日工单数"/>
            </el-col>
            <el-col :span="8">
              <StaticPannel icon="fa-file-text-o" :num="comtk" type="warning" text="已完成数量"/>
            </el-col>
            <el-col :span="8">
              <StaticPannel icon="fa-comments-o" :num="nonetk" type="danger" text="未完成数量"/>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <div style="text-align: left;">
          <el-card :body-style="{ padding: '0px' }">
            <div slot="header" class="clearfix">
              <span>科室纪事</span>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="12">
        <div style="text-align: left;">
          <el-card :body-style="{ padding: '0px' }">
            <div slot="header" class="clearfix">
              <span>管理资金使用记录</span>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
  import StaticPannel from "../common/StaticPannel.vue";
  export default {
    data() {
      return {
        name: '',
        loading: false,
        todaytk: 0,
        comtk: 0,
        nonetk: 0,
        loading: false,
      };
    },
    components: {
      StaticPannel,
    },
    computed: {
      user(){
        return this.$store.state.user;
      },
      routes(){
        return this.$store.state.routes
      }
    },
    mounted: function() {
      this.initTicketNum()
    },
    methods: {
      initTicketNum() {
        var _this = this;
        _this.loading = true;
        _this.getRequest('/ticket/tkcount').then(resp=>{
          _this.loading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            //console.log(data)
            _this.todaytk = data.jr;
            _this.comtk = data.wc;
            _this.nonetk = data.ww;
          }
        })
      }
    },
  };
</script>
<style lang="scss">
  .admin-main {
    padding: 30px;
  }
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 15px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both;
  }

  .pannel-box {
    margin-bottom: 10px;
    ul {
      margin: 0;
      padding: 0;
      li {
        list-style-type: none;
      }
    }
    .quick-opt {
      li {
        display: inline-block;
        margin: 5px 2px;
      }
    }

    .basic-count {
      li {
        color: #5a5e66;
        line-height: 25px;
        span {
          color: #409eff;
          font-style: oblique;
        }
      }
    }
    .user-list {
      li {
        display: inline-block;
        margin-bottom: 10px;
        position: relative;
        width: 100px;
        img {
          border-radius: 50%;
          max-width: 75%;
          height: auto;
          width: 26px;
          position: absolute;
        }
        span {
          height: 26px;
          line-height: 26px;
          margin-left: 30px;
        }
      }
    }
    .direct-chat-msg {
      margin-bottom: 8px;
      a:link,
      a:visited {
        color: #409eff;
      }
      .direct-chat-contentTitle {
        font-size: 12px;
      }
      .direct-chat-contentTitle:link,
      .direct-chat-contentTitle:visited {
        color: #878d99;
        font-style: italic;
      }
      .direct-chat-info {
        margin-bottom: 3px;
      }
      .direct-chat-timestamp {
        color: #b4bccc;
        font-size: 12px;
      }
      .direct-chat-img {
        border-radius: 50%;
        width: 35px;
        height: 35px;
        float: left;
      }
      .direct-chat-text {
        border-radius: 5px;
        position: relative;
        padding: 5px 10px;
        margin: 5px 0 0 50px;
        color: #5a5e66;
        background-color: #edf2fc;
        font-size: 13px;
      }
      .direct-chat-text:after,
      .direct-chat-text:before {
        position: absolute;
        right: 100%;
        top: 15px;
        border: solid transparent;
        border-right-color: #edf2fc;
        content: " ";
        height: 0;
        width: 0;
        pointer-events: none;
        box-sizing: border-box;
      }
      .direct-chat-text:before {
        border-width: 6px;
        margin-top: -6px;
      }
      .direct-chat-text:after {
        border-width: 5px;
        margin-top: -5px;
      }
    }
  }

  .user-basic-info {
    .logo-pannel {
      border-bottom: 1px solid #edf2fc;
      padding-bottom: 12px;
      .logo {
        float: left;
        width: 100%;
        img {
          width: 120px;
          max-width: 120px;
          height: 120px;
          border-radius: 50%;
        }
      }
      .name {
        float: right;
        width: 100%;
        h3 {
          font-size: 1.6rem;
          color: #409eff;
          margin-top: 1rem;
          margin-bottom: 0.2rem;
        }
        span {
          color: #878d99;
          font-size: 13px;
        }
      }
    }
    .info-pannel {
      padding-top: 12px;
      ul {
        li {
          line-height: 25px;
          color: #5a5e66;
          font-size: 14px;
          label {
            display: inline-block;
            width: 33%;
            margin-right: 20px;
          }
          .el-button--text {
            padding: 0;
          }
        }
      }
    }
  }
</style>
