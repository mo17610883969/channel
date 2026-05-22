<template>
  <div class="menu-container">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd(null)">
        <el-icon><Plus /></el-icon>新增根菜单
      </el-button>
    </div>

    <el-table
      :data="menuList"
      row-key="id"
      border
      stripe
      v-loading="loading"
      style="width: 100%; margin-top: 16px"
      default-expand-all
    >
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="menuName" label="菜单名称" min-width="180" />
      <el-table-column prop="icon" label="图标" width="100" align="center">
        <template #default="{ row }">
          <el-icon v-if="row.icon" :size="18">
            <component :is="row.icon" />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="menuType" label="菜单类型" width="110" align="center">
        <template #default="{ row }">
          <el-tag :type="row.menuType === 'CATALOG' ? '' : row.menuType === 'MENU' ? 'success' : 'warning'" size="small">
            {{ row.menuType === 'CATALOG' ? '目录' : row.menuType === 'MENU' ? '菜单' : '按钮' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路由路径" width="180" />
      <el-table-column prop="component" label="组件路径" width="200" />
      <el-table-column prop="permission" label="权限标识" width="180" />
      <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleAdd(row)">新增子菜单</el-button>
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级菜单">
          <el-input :value="parentMenuName" disabled />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-select v-model="form.menuType" placeholder="请选择菜单类型" style="width: 100%">
            <el-option label="目录" value="CATALOG" />
            <el-option label="菜单" value="MENU" />
            <el-option label="按钮" value="BUTTON" />
          </el-select>
        </el-form-item>
        <el-form-item label="路由路径" prop="path" v-if="form.menuType !== 'BUTTON'">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="form.menuType === 'MENU'">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入Element图标名称" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="form.permission" placeholder="如 sys:user:list" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getMenuList, addMenu, updateMenu, deleteMenu } from '@/api/menu'

const loading = ref(false)
const menuList = ref([])
const parentMenuName = ref('根目录')

const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const formRef = ref(null)
const form = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  menuType: 'MENU',
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  permission: '',
  status: 1
})

const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }]
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getMenuList()
    menuList.value = buildTree(res.data)
  } catch (error) {
    console.error('获取菜单列表失败', error)
  } finally {
    loading.value = false
  }
}

function buildTree(list) {
  const map = {}
  const result = []
  list.forEach(item => {
    item.children = []
    map[item.id] = item
  })
  list.forEach(item => {
    if (item.parentId === 0) {
      result.push(item)
    } else {
      const parent = map[item.parentId]
      if (parent) {
        parent.children.push(item)
      }
    }
  })
  return result
}

function getParentName(menu) {
  if (!menu) return '根目录'
  return menu.menuName
}

function handleAdd(parentMenu) {
  dialogTitle.value = parentMenu ? `新增子菜单 - ${parentMenu.menuName}` : '新增根菜单'
  parentMenuName.value = getParentName(parentMenu)
  form.id = null
  form.parentId = parentMenu ? parentMenu.id : 0
  form.menuName = ''
  form.menuType = 'MENU'
  form.path = ''
  form.component = ''
  form.icon = ''
  form.sortOrder = 0
  form.permission = ''
  form.status = 1
  dialogVisible.value = true
  setTimeout(() => formRef.value?.resetFields(), 0)
}

function handleEdit(row) {
  dialogTitle.value = '编辑菜单'
  const parent = findParent(menuList.value, row.parentId)
  parentMenuName.value = getParentName(parent)
  form.id = row.id
  form.parentId = row.parentId
  form.menuName = row.menuName
  form.menuType = row.menuType
  form.path = row.path || ''
  form.component = row.component || ''
  form.icon = row.icon || ''
  form.sortOrder = row.sortOrder
  form.permission = row.permission || ''
  form.status = row.status
  dialogVisible.value = true
}

function findParent(tree, parentId) {
  for (const item of tree) {
    if (item.id === parentId) return item
    if (item.children && item.children.length) {
      const found = findParent(item.children, parentId)
      if (found) return found
    }
  }
  return null
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (form.id) {
      await updateMenu({ ...form })
      ElMessage.success('修改成功')
    } else {
      await addMenu({ ...form })
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('操作失败', error)
  }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确认删除菜单【${row.menuName}】？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.menu-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.toolbar {
  display: flex;
  align-items: center;
}
</style>