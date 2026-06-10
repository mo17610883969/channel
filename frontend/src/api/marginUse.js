import request from './request'

// 账户使用列表
export function getUseList(params) {
  return request({
    url: '/margin/account/use/list',
    method: 'get',
    params
  })
}

// 保存账户使用
export function saveUse(data) {
  return request({
    url: '/margin/account/use/save',
    method: 'post',
    data
  })
}

// 更新账户使用
export function updateUse(data) {
  return request({
    url: '/margin/account/use/update',
    method: 'put',
    data
  })
}

// 获取详情
export function getUseDetails(id) {
  return request({
    url: `/margin/account/use/details/${id}`,
    method: 'get'
  })
}

// 审核使用
export function approveUse(data) {
  return request({
    url: '/margin/account/use/approve',
    method: 'post',
    data
  })
}

// 提交释放
export function submitRelease(data) {
  return request({
    url: '/margin/account/use/submitRelease',
    method: 'post',
    data
  })
}

// 审核释放
export function approveRelease(data) {
  return request({
    url: '/margin/account/use/approveRelease',
    method: 'post',
    data
  })
}

// 检查订单号
export function checkBussNo(bussNo) {
  return request({
    url: '/margin/account/use/checkBussNo',
    method: 'get',
    params: { bussNo }
  })
}