<template>
  <div class="margin-use-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>保证金账户使用列表</span>
          <el-button type="primary" @click="handleAdd">新增账户使用</el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="主渠道名称">
          <el-input v-model="searchForm.masterChannelName" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="订单号">
          <el-input v-model="searchForm.bussNo" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.custName" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="待审核" value="0" />
            <el-option label="使用中" value="1" />
            <el-option label="审核拒绝" value="2" />
            <el-option label="释放审核中" value="3" />
            <el-option label="已释放" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="masterChannelName" label="主渠道名称" min-width="120" />
        <el-table-column prop="name" label="渠道名称" min-width="100" />
        <el-table-column prop="code" label="渠道编码" width="100" />
        <el-table-column prop="bussNo" label="订单号" min-width="120" />
        <el-table-column prop="custName" label="客户姓名" width="100" />
        <el-table-column prop="contAmt" label="借款金额" width="120" :formatter="formatMoney" />
        <el-table-column prop="marginUseBalance" label="使用保证金" width="120" :formatter="formatMoney" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creater" label="申请人" width="100" />
        <el-table-column prop="createTime" label="申请时间" width="160" :formatter="formatDate" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">详情</el-button>
            <el-button link type="primary" @click="handleApprove(row)" v-if="row.status === '0'">审核</el-button>
            <el-button link type="success" @click="handleRelease(row)" v-if="row.status === '1'">释放</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <!-- 新增对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-form-item label="订单号" prop="bussNo">
          <el-input v-model="formData.bussNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="客户姓名" prop="custName">
          <el-input v-model="formData.custName" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="合同号" prop="contractNo">
          <el-input v-model="formData.contractNo" placeholder="请输入合同号" />
        </el-form-item>
        <el-form-item label="借款金额" prop="loanAmt">
          <el-input-number v-model="formData.loanAmt" :min="0" :precision="2" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="使用保证金" prop="marginUseBalance">
          <el-input-number v-model="formData.marginUseBalance" :min="0" :precision="2" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">提交</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="账户使用详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="主渠道名称">{{ detailData.masterChannelName }}</el-descriptions-item>
        <el-descriptions-item label="渠道名称">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="订单号">{{ detailData.bussNo }}</el-descriptions-item>
        <el-descriptions-item label="合同号">{{ detailData.contractNo }}</el-descriptions-item>
        <el-descriptions-item label="客户姓名">{{ detailData.custName }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusText(detailData.status) }}</el-descriptions-item>
        <el-descriptions-item label="借款金额">{{ detailData.contAmt }}</el-descriptions-item>
        <el-descriptions-item label="使用保证金">{{ detailData.marginUseBalance }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.creater }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog v-model="approveVisible" title="审核账户使用" width="400px">
      <el-form :model="approveForm" label-width="80px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="approveForm.firstApprStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="approveForm.firstApprDesc" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApproveSubmit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUseList, saveUse, approveUse, submitRelease } from '@/api/marginUse'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const detailVisible = ref(false)
const approveVisible = ref(false)
const dialogTitle = ref('新增账户使用')

const searchForm = reactive({ masterChannelName: '', bussNo: '', custName: '', status: '' })
const pagination = reactive({ current: 1, size: 10, total: 0 })

const formData = reactive({ bussNo: '', custName: '', contractNo: '', loanAmt: 0, marginUseBalance: 0, remark: '' })
const formRules = {
  bussNo: [{ required: true, message: '请输入订单号', trigger: 'blur' }],
  custName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  marginUseBalance: [{ required: true, message: '请输入使用保证金', trigger: 'blur' }]
}

const detailData = ref({})
const approveForm = reactive({ firstApprStatus: '1', firstApprDesc: '' })
let currentRow = null

const statusMap = { '0': { text: '待审核', type: 'warning' }, '1': { text: '使用中', type: 'success' }, '2': { text: '审核拒绝', type: 'danger' }, '3': { text: '释放审核中', type: 'warning' }, '4': { text: '已释放', type: 'info' } }
const getStatusText = (status) => statusMap[status]?.text || status
const getStatusType = (status) => statusMap[status]?.type || ''
const formatMoney = (row, col, val) => val ? `¥${val.toLocaleString('zh-CN', { minimumFractionDigits: 2 })}` : '-'
const formatDate = (row, col, val) => val ? new Date(val).toLocaleString() : '-'

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUseList({ current: pagination.current, size: pagination.size, ...searchForm })
    if (res.code === 200) {
      tableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    }
  } finally { loading.value = false }
}

const handleSearch = () => { pagination.current = 1; loadData() }
const handleReset = () => { Object.keys(searchForm).forEach(k => searchForm[k] = ''); handleSearch() }
const handleAdd = () => { dialogTitle.value = '新增账户使用'; Object.keys(formData).forEach(k => formData[k] = typeof formData[k] === 'number' ? 0 : ''); dialogVisible.value = true }
const handleView = (row) => { detailData.value = row; detailVisible.value = true }
const handleApprove = (row) => { currentRow = row; approveForm.firstApprStatus = '1'; approveForm.firstApprDesc = ''; approveVisible.value = true }

const handleApproveSubmit = async () => {
  try {
    await approveUse({ marginApproveId: currentRow.id, firstApprStatus: approveForm.firstApprStatus, firstApprDesc: approveForm.firstApprDesc })
    ElMessage.success('审核完成'); approveVisible.value = false; loadData()
  } catch { ElMessage.error('审核失败') }
}

const handleRelease = async (row) => {
  try {
    await ElMessageBox.confirm('确定要提交释放申请吗？', '提示', { type: 'warning' })
    await submitRelease({ operationId: row.id })
    ElMessage.success('提交成功'); loadData()
  } catch (e) { if (e !== 'cancel') ElMessage.error('提交失败') }
}

const handleSubmit = async () => {
  try {
    submitLoading.value = true
    await saveUse(formData)
    ElMessage.success('提交成功'); dialogVisible.value = false; loadData()
  } catch { ElMessage.error('提交失败') } finally { submitLoading.value = false }
}

onMounted(() => { loadData() })
</script>

<style scoped>
.margin-use-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>