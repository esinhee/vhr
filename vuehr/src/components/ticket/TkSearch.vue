<template>
  <div>
    <el-container>
      <el-header style="padding-left: 0px;padding-top: 20px">
        <div style="margin-bottom: 10px;border: 1px;border-radius: 5px;border-style: solid;padding: 15px 0px 15px 10px;box-sizing:border-box;border-color: #20a0ff">
          <el-row style="text-align: left;padding-bottom: 10px">
            <el-col :span="8" >
              重要程度:
              <el-select v-model="et.grade" placeholder="请选择" size="mini">
                <el-option
                  v-for="item in grades"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              问题类型:
              <!--<el-select v-model="et.tktype" placeholder="请选择" size="mini">
                <el-option
                  v-for="item in tktypes"
                  :key="item.id"
                  :label="item.typename"
                  :value="item.id">
                </el-option>
              </el-select>-->
              <el-cascader
                :options="tktypes"
                :props="defaultProps"
                v-model="et.tktype"
                :clearable="true"
                expand-trigger="hover"
                :show-all-levels="false"
                change-on-select
                size="mini"
                :before-filter="filterNode"
              ></el-cascader>
            </el-col>
            <el-col :span="8">
              处理状态:
              <el-select v-model="et.tkstatus" placeholder="请选择" size="mini">
                <el-option
                  v-for="item in tkstatuss"
                  :key="item.zid"
                  :label="item.zname"
                  :value="item.zid">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row style="text-align: left">
            <el-col :span="8">
              处理人员:
              <el-select v-model="et.dealer" placeholder="请选择" size="mini">
                <el-option
                  v-for="item in dealers"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              查询日期:
              <el-date-picker
                v-model="et.beginDate"
                unlink-panels
                size="mini"
                type="daterange"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-button icon="el-icon-search" type="primary" size="mini" @click="search">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </el-header>
      <el-main style="padding-top: 70px">
        <div>
          <el-table
            v-loading="loading"
            :data="elist"
            size="mini"
            stripe
            border
            @selection-change="handleSelectionChange"
            style="width: 90%">
            <el-table-column type="expand">
              <template slot-scope="props">
                <span style="text-align: left" v-html="props.row.content"></span>
              </template>
            </el-table-column>
            <el-table-column
              type="selection"
              width="40"
              align="left">
            </el-table-column>
            <el-table-column
              prop="eid"
              label="编号"
              width="60"
              align="left">
            </el-table-column>
            <el-table-column
              prop="etname"
              width="100"
              label="类型名称"
              align="left">
            </el-table-column>
            <el-table-column
              prop="grade"
              width="70"
              label="重要程度"
              align="left">
              <template slot-scope="scope">
                <el-tag
                  v-if="scope.row.grade === 2"
                  type="warning"
                  size="mini"
                  style="margin-right: 5px"
                  :disable-transitions="false">重要
                </el-tag>
                <el-tag
                  v-else-if="scope.row.grade === 3"
                  type="danger"
                  size="mini"
                  style="margin-right: 5px"
                  :disable-transitions="false">特大
                </el-tag>
                <el-tag
                  v-else
                  size="mini"
                  style="margin-right: 5px"
                  :disable-transitions="false">普通
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="kname"
              width="100"
              label="反馈科室"
              align="left">
            </el-table-column>
            <el-table-column
              prop="reporter"
              width="100"
              label="反馈人"
              align="left">
            </el-table-column>
            <el-table-column
              prop="reporttime"
              width="100"
              label="反馈日期"
              align="left">
            </el-table-column>
            <el-table-column
              prop="dealer"
              width="70"
              label="分配人"
              align="left">
            </el-table-column>
            <el-table-column
              prop="zname"
              width="80"
              label="工单状态"
              align="left">
            </el-table-column>
            <el-table-column label="操作" align="left">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleView(scope.$index, scope.row)">查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="text-align: left;margin-top: 10px">
            <el-pagination
              background
              @current-change="currentChange"
              layout="prev, pager, next"
              :total="totalCount">
            </el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
    export default {
      data() {
        return {
          et: {
            grade: '',
            tktype: [],
            tkstatus: '',
            dealer: '',
            beginDate: '',
          },
          grades: [{
            value: 1,
            label: '普通'
          },{
            value: 2,
            label: '重要'
          },{
            value: 3,
            label: '特急'
          }],
          tktypes: [],
          tkstatuss: [],
          totalCount: -1,
          currentPage: 1,
          elist: [],
          dealers: [],
          loading: false,
          defaultProps: {
            label: 'typename',
            value: 'id',
            children: 'children'
          }
        }
      },
      mounted: function() {
        this.getTkTypeList();
        this.getTkStatusList();
        this.getCzryList();
      },
      methods: {
        handleSelectionChange(val) {
          this.multipleSelection = val;
        },
        filterNode(value) {
          return true;
        },
        getTkTypeList() {
          var _this = this;
          _this.getRequest("/ticket/tttoplist/0").then(resp=> {
            if (resp && resp.status == 200) {
              this.tktypes = resp.data;
              if (!this.tktypes) return;
              this.tktypes.forEach(function (r) {
                _this.traverseTree(r);
              })

            }
          });
        },
        traverseTree(node) {
          if (!node) return;
          if (node.children && node.children.length > 0) {
            var i = 0;
            for (i = 0; i < node.children.length; i++) {
              this.traverseTree(node.children[i]);
            }
          } else {
            node.children = null;
          }
        },
        getTkStatusList() {
          var _this = this;
          _this.getRequest("/ticket/getstatuss").then(resp=> {
            if (resp && resp.status == 200) {
              _this.tkstatuss = resp.data;
            }
          });
        },
        getCzryList() {
          var _this = this;
          this.getRequest("/ticket/rylist").then(resp=> {
            if (resp && resp.status == 200) {
              _this.dealers = resp.data;
            }
          });
        },
        search(){
          var _this = this;
          _this.loading = true;
          _this.getRequest("/ticket/searchtk?page=" + _this.currentPage + "&size=10&grade=" + _this.et.grade + "&tktype=" + _this.et.tktype + "&tkstatus=" + _this.et.tkstatus + "&dealer=" + _this.et.dealer + "&beginDate=" + _this.et.beginDate).then(resp=>{
            _this.loading = false;
            if (resp && resp.status == 200) {
              var data = resp.data;
              _this.elist = data.tlist;
              _this.totalCount = data.count;
            }
          })
        },
        handleView(index, row){
          this.$router.push({path:'/ticket/view', query:{id: row.eid}});
        },
        currentChange(currentChange) {
          this.currentPage = currentChange;
          this.search();
        },
      },
      computed: {
        user(){
          return this.$store.state.user;
        },
      },
    }
</script>

<style scoped>

</style>
