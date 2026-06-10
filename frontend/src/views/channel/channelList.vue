<template>
  <div class="channel-list">
    <el-card>
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="渠道编码">
            <el-input v-model="searchForm.code" placeholder="请输入渠道编码" clearable />
          </el-form-item>
          <el-form-item label="渠道名称">
            <el-input v-model="searchForm.name" placeholder="请输入渠道名称" clearable />
          </el-form-item>
          <el-form-item label="渠道属性">
            <el-select v-model="searchForm.channelAttribute" placeholder="请选择" clearable>
              <el-option label="个人" value="1" />
              <el-option label="机构" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button type="primary" @click="handleAdd">新增</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="code" label="渠道编码" width="130" />
        <el-table-column prop="name" label="渠道简称" min-width="150" />
        <el-table-column prop="fullName" label="渠道全称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="channelAttribute" label="渠道属性" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.channelAttribute === '1'" type="info">个人</el-tag>
            <el-tag v-else-if="row.channelAttribute === '2'" type="success">机构</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="area" label="所在区域" width="100" />
        <el-table-column prop="ownerName" label="商务经理" width="100" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column prop="able" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.able === '0' ? 'success' : 'danger'">
              {{ row.able === '0' ? '可用' : '不可用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @change="loadData"
        />
      </div>
    </el-card>

    <!-- 查看/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="渠道简称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入渠道简称" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="渠道全称" prop="fullName">
              <el-input v-model="formData.fullName" placeholder="请输入渠道全称" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="渠道属性" prop="channelAttribute">
              <el-select v-model="formData.channelAttribute" placeholder="请选择" :disabled="isView">
                <el-option label="个人" value="1" />
                <el-option label="机构" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所在区域" prop="area">
              <el-input v-model="formData.area" placeholder="请输入所在区域" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市区域" prop="cityArea">
              <el-input v-model="formData.cityArea" placeholder="请输入城市区域" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证件号码" prop="idcard">
              <el-input v-model="formData.idcard" placeholder="请输入证件号码" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通讯地址" prop="communicationAddress">
              <el-input v-model="formData.communicationAddress" placeholder="请输入通讯地址" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>银行信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开户银行" prop="openBank">
              <el-input v-model="formData.openBank" placeholder="请输入开户银行" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="户名" prop="account">
              <el-input v-model="formData.account" placeholder="请输入户名" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行账号" prop="bankAccount">
              <el-input v-model="formData.bankAccount" placeholder="请输入银行账号" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配资标识" prop="capitalAllocation">
              <el-select v-model="formData.capitalAllocation" placeholder="请选择" :disabled="isView">
                <el-option label="是" value="1" />
                <el-option label="否" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider>其他信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="注册资金" prop="registeCapital">
              <el-input v-model="formData.registeCapital" placeholder="请输入注册资金" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司规模" prop="firmScale">
              <el-input v-model="formData.firmScale" placeholder="请输入公司规模" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="展业模式" prop="exhibition">
              <el-input v-model="formData.exhibition" placeholder="请输入展业模式" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主营业务" prop="majorBusiness">
              <el-input v-model="formData.majorBusiness" placeholder="请输入主营业务" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="comment">
              <el-input v-model="formData.comment" type="textarea" :rows="2" placeholder="请输入备注" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span v-if="!isView">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
        <span v-else>
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="handleEditBtn">编辑</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getChannelPage, getChannelByCode, addChannel, updateChannel, deleteChannel } from '@/api/channel'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const loading = ref(false)
const tableData = ref([])

const searchForm = reactive({
  code: '',
  name: '',
  channelAttribute: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增渠道')
const isView = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  code: '',
  name: '',
  fullName: '',
  channelAttribute: '',
  area: '',
  cityArea: '',
  phone: '',
  idcard: '',
  communicationAddress: '',
  openBank: '',
  account: '',
  bankAccount: '',
  capitalAllocation: '0',
  registeCapital: '',
  firmScale: '',
  exhibition: '',
  exhibitionRemark: '',
  majorBusiness: '',
  majorBusinessRemark: '',
  comment: ''
})

const formRules = {
  name: [{ required: true, message: '请输入渠道简称', trigger: 'blur' }],
  fullName: [{ required: true, message: '请输入渠道全称', trigger: 'blur' }],
  channelAttribute: [{ required: true, message: '请选择渠道属性', trigger: 'change' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getChannelPage(params)
    if (res.code === 200) {
      tableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  searchForm.code = ''
  searchForm.name = ''
  searchForm.channelAttribute = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增渠道'
  isView.value = false
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleView = async (row) => {
  try {
    const res = await getChannelByCode(row.code)
    if (res.code === 200) {
      Object.assign(formData, res.data)
      dialogTitle.value = '查看渠道'
      isView.value = true
      isEdit.value = false
      dialogVisible.value = true
    }
  } catch (error) {
    console.error(error)
  }
}

const handleEdit = async (row) => {
  try {
    const res = await getChannelByCode(row.code)
    if (res.code === 200) {
      Object.assign(formData, res.data)
      dialogTitle.value = '编辑渠道'
      isView.value = false
      isEdit.value = true
      dialogVisible.value = true
    }
  } catch (error) {
    console.error(error)
  }
}

const handleEditBtn = () => {
  isView.value = false
  isEdit.value = true
  dialogTitle.value = '编辑渠道'
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let res
        if (isEdit.value) {
          res = await updateChannel(formData)
        } else {
          res = await addChannel(formData)
        }
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(res.message || '操作失败')
        }
      } catch (error) {
        console.error(error)
        ElMessage.error('操作失败')
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该渠道吗？删除后渠道将移入回收站。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteChannel(row.code)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        loadData()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    code: '',
    name: '',
    fullName: '',
    channelAttribute: '',
    area: '',
    cityArea: '',
    phone: '',
    idcard: '',
    communicationAddress: '',
    openBank: '',
    account: '',
    bankAccount: '',
    capitalAllocation: '0',
    registeCapital: '',
    firmScale: '',
    exhibition: '',
    exhibitionRemark: '',
    majorBusiness: '',
    majorBusinessRemark: '',
    comment: ''
  })
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.channel-list {
  width: 100%;
}

.search-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-divider {
  margin: 10px 0;
}
</style>