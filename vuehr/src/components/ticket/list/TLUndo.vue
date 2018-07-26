<template>
  <div>
    <div style="margin-top: 10px">
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
            <el-button
              v-if="reassign(scope.row)"
              size="mini"
              @click="handleReassign(scope.$index, scope.row)"
              type="success">分配
            </el-button>
            <el-button
              v-if="refuseflg(scope.row)"
              size="mini"
              @click="handleRefuse(scope.$index, scope.row)"
              type="danger">拒绝
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
    <div style="text-align: left">
      <el-dialog
        title="工单分配"
        :visible.sync="dialogVisible"
        width="25%">
        <el-select v-model="updateDealer" style="margin-top: 10px" size="mini"
                   value-key="id" placeholder="请选择处理人员..">
          <el-option
            v-for="item in czrys"
            :key="item.id"
            :label="item.name"
            :value="item">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.id }}</span>
          </el-option>
        </el-select>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" size="mini" @click="updateReassign">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return {
        loading: false,
        dialogVisible: false,
        multipleSelection: [],
        elist: [],
        totalCount: -1,
        currentPage: 1,
        updateDealer: '',
        updateDealerid: 0,
        updateEid: 0,
        czrys: [],
      }
    },
    mounted: function () {
      this.loadTableData();
    },
    methods: {
      newTicket() {
        this.$router.push({path: '/ticket/new'})
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      handleView(index, row){
        this.$router.push({path:'/ticket/view', query:{id: row.eid}});
      },
      handleRefuse(index, row){
        var _this = this;
        this.$confirm('拒绝[' + row.eid + ']工单, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.doRefuse(row.id);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '操作取消'
          });
        });
      },
      handleReassign(index, row) {
        var _this = this;
        this.getRequest("/ticket/oplist").then(resp=> {
          if (resp && resp.status == 200) {
            _this.czrys = resp.data;
          }
        });
        _this.updateEid = row.eid;
        _this.updateDealerid = row.dealerid;
        this.dialogVisible = true;
      },
      doRefuse(ids){
        var _this = this;
        _this.loading = true;
        this.deleteRequest('/ticket/tkrefuse').then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.loadTableData();
          }
        })
      },
      loadTableData(){
        var _this = this;
        this.loading = true;
        this.getRequest("/ticket/tlundo").then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.elist = data;
          }
        })
      },
      refuseflg (row) {
        if (row.recorderid != row.dealerid && this.user.id == row.dealerid && row.dealflg<7) {
          return true;
        } else {
          return false;
        }
      },
      reassign(row) {
        //分配按钮显示，
        if ((this.user.id == row.recorderid && row.recorderid != row.dealerid && row.dealerflg != 10)) {
          return true;
        } else {
          return false;
        }
      },
      updateReassign() {
        var _this = this;
        if (!_this.isNotNullORBlank(this.updateDealer)) {
          this.$message.warning('请指定处理人员!');
          return;
        }
        if (_this.updateDealerid == _this.updateDealer.id) {
          this.$message.warning('处理人员没有修改!');
          return;
        }
        const params = {
          eid: _this.updateEid,
          userid: _this.updateDealer.id,
          name: _this.updateDealer.name
        }
        _this.putRequest('/ticket/reassign', params).then(resp=>{
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.$message({type: data.status, message: data.msg});
            _this.loadTableData();
            _this.updateEid = 0;
            _this.updateDealerid = 0;
            _this.dialogVisible = false;
          }
        })
      },
      currentChange(currentChange) {
        this.currentPage = currentChange;
        this.loadTableData();
      },
    },
    computed: {
      user(){
        return this.$store.state.user;
      },
    },
  }
</script>
