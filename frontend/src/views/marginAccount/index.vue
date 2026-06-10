<template>
  <div class="margin-account-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>渠道保证金账户列表</span>
          <el-button type="primary" @click="handleAdd">新增账户</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="masterChannelName" label="渠道名称" min-width="120" />
        <el-table-column prop="channelCodes" label="渠道编码" min-width="120" />
        <el-table-column prop="channelNames" label="关联渠道" min-width="150" />
        <el-table-column prop="cityNames" label="城市" min-width="100" />
        <el-table-column label="累缴保证金(元)" min-width="130">
          <template #default="{ row }">
            {{ formatAmount(row.marginFirstBalance) }}
          </template>
        </el-table-column>
        <el-table-column label="可用保证金(元)" min-width="130">
          <template #default="{ row }">
            {{ formatAmount(row.kyBzjBalance) }}
          </template>
        </el-table-column>
        <el-table-column label="留底保证金(元)" min-width="130">
          <template #default="{ row }">
            {{ formatAmount(row.marginRemainBalance) }}
          </template>
        </el-table-column>
        <el-table-column label="待退保证金(元)" min-width="130">
          <template #default="{ row }">
            {{ formatAmount(row.marginPendingRefundBalance) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">详情</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleClose(row)" v-if="row.status !== '4'">关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" @close="handleDialogClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="渠道名称" prop="masterChannelName">
          <el-input v-model="form.masterChannelName" placeholder="请输入渠道名称" />
        </el-form-item>
        <el-form-item label="开户名称" prop="accountName">
          <el-input v-model="form.accountName" placeholder="请输入开户名称" />
        </el-form-item>
        <el-form-item label="银行账号" prop="bankAccount">
          <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
        </el-form-item>
        <el-form-item label="首缴保证金" prop="marginFirstBalance">
          <el-input-number v-model="form.marginFirstBalance" :min="0" :precision="4" placeholder="万元" />
          <span class="unit">万元</span>
        </el-form-item>
        <el-form-item label="留底保证金" prop="marginRemainBalance">
          <el-input-number v-model="form.marginRemainBalance" :min="0" :precision="4" placeholder="万元" />
          <span class="unit">万元</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="保证金账户详情" width="900px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="渠道名称">{{ currentRow.masterChannelName }}</el-descriptions-item>
        <el-descriptions-item label="开户名称">{{ currentRow.accountName }}</el-descriptions-item>
        <el-descriptions-item label="银行账号">{{ currentRow.bankAccount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRow.status)">{{ getStatusText(currentRow.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="累缴保证金(元)">{{ formatAmount(currentRow.marginFirstBalance) }}</el-descriptions-item>
        <el-descriptions-item label="充值金额(元)">{{ formatAmount(currentRow.rechargeBalance) }}</el-descriptions-item>
        <el-descriptions-item label="可用保证金(元)">{{ formatAmount(currentRow.kyBzjBalance) }}</el-descriptions-item>
        <el-descriptions-item label="留底保证金(元)">{{ formatAmount(currentRow.marginRemainBalance) }}</el-descriptions-item>
        <el-descriptions-item label="在途使用(元)">{{ formatAmount(currentRow.marginTransitUseBalance) }}</el-descriptions-item>
        <el-descriptions-item label="已退保证金(元)">{{ formatAmount(currentRow.marginRefundedBalance) }}</el-descriptions-item>
        <el-descriptions-item label="待退保证金(元)">{{ formatAmount(currentRow.marginPendingRefundBalance) }}</el-descriptions-item>
        <el-descriptions-item label="账面余额(元)">{{ formatAmount(currentRow.zmbzjAmount) }}</el-descriptions-item>
        <el-descriptions-item label="实际可退(元)">{{ formatAmount(currentRow.sjktAmount) }}</el-descriptions-item>
        <el-descriptions-item label="关联渠道">{{ currentRow.channelNames }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark }}</el-descriptions-item>
      </el-descriptions>

      <div class="action-buttons" v-if="currentRow.status === '0'">
        <el-button type="success" @click="handleApprove(currentRow, '1')">审核通过</el-button>
        <el-button type="danger" @click="handleApprove(currentRow, '2')">审核拒绝</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getMarginAccountList,
  getMarginAccount,
  saveMarginAccount,
  updateMarginAccount,
  approveMarginAccount,
  rechargeMarginAccount,
  refundMarginAccount,
  closeMarginAccount,
  getChannelList
} from '@/api/marginAccount'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const detailVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()
const currentRow = ref({})

const form = ref({
  masterChannelName: '',
  accountName: '',
  bankAccount: '',
  marginFirstBalance: 0,
  marginRemainBalance: 0,
  remark: ''
})

const rules = {
  masterChannelName: [{ required: true, message: '请输入渠道名称', trigger: 'blur' }],
  accountName: [{ required: true, message: '请输入开户名称', trigger: 'blur' }],
  bankAccount: [{ required: true, message: '请输入银行账号', trigger: 'blur' }],
  marginFirstBalance: [{ required: true, message: '请输入首缴保证金', trigger: 'blur' }]
}

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getMarginAccountList()
    tableData.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

function handleAdd() {
  dialogTitle.value = '新增保证金账户'
  form.value = {
    masterChannelName: '',
    accountName: '',
    bankAccount: '',
    marginFirstBalance: 0,
    marginRemainBalance: 0,
    remark: ''
  }
  dialogVisible.value = true
}

async function handleEdit(row) {
  dialogTitle.value = '编辑保证金账户'
  const res = await getMarginAccount(row.id)
  const data = res.data
  form.value = {
    id: data.id,
    masterChannelName: data.masterChannelName,
    accountName: data.accountName,
    bankAccount: data.bankAccount,
    marginFirstBalance: data.marginFirstBalance,
    marginRemainBalance: data.marginRemainBalance,
    remark: data.remark
  }
  dialogVisible.value = true
}

async function handleView(row) {
  const res = await getMarginAccount(row.id)
  currentRow.value = res.data || {}
  detailVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    if (form.value.id) {
      await updateMarginAccount(form.value)
      ElMessage.success('更新成功')
    } else {
      await saveMarginAccount(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

function handleDialogClose() {
  formRef.value?.resetFields()
}

async function handleApprove(row, status) {
  const action = status === '1' ? '通过' : '拒绝'
  try {
    await ElMessageBox.confirm(`确定要${action}该账户吗?`, '提示', { type: 'warning' })
    await approveMarginAccount({ id: row.id, status })
    ElMessage.success(`已${action}`)
    detailVisible.value = false
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('操作失败')
  }
}

async function handleClose(row) {
  try {
    await ElMessageBox.confirm('确定要关闭该保证金账户吗?', '提示', { type: 'warning' })
    await closeMarginAccount({ id: row.id })
    ElMessage.success('已关闭')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('操作失败')
  }
}

function formatAmount(val) {
  if (val == null) return '-'
  return typeof val === 'number' ? val.toFixed(4) : val
}

function getStatusType(status) {
  const map = { '0': 'warning', '1': 'success', '3': 'info', '4': 'danger' }
  return map[status] || ''
}

function getStatusText(status) {
  const map = { '0': '审核中', '1': '已审核', '3': '关闭处理中', '4': '已关闭' }
  return map[status] || status
}
</script>

<style scoped>
.margin-account-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.unit {
  margin-left: 8px;
  color: #999;
}
.action-buttons {
  margin-top: 20px;
  text-align: center;
}
</style>