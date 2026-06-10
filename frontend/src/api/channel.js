import request from './request'

/**
 * 获取渠道信息列表（分页）
 */
export function getChannelPage(params) {
  return request({
    url: '/channel/list',
    method: 'get',
    params
  })
}

/**
 * 根据编码获取渠道详情
 */
export function getChannelByCode(code) {
  return request({
    url: `/channel/${code}`,
    method: 'get'
  })
}

/**
 * 新增渠道
 */
export function addChannel(data) {
  return request({
    url: '/channel',
    method: 'post',
    data
  })
}

/**
 * 更新渠道
 */
export function updateChannel(data) {
  return request({
    url: '/channel',
    method: 'put',
    data
  })
}

/**
 * 删除渠道（移入回收站）
 */
export function deleteChannel(code) {
  return request({
    url: `/channel/${code}`,
    method: 'delete'
  })
}

/**
 * 获取渠道下拉选项
 */
export function getChannelOptions() {
  return request({
    url: '/channel/options',
    method: 'get'
  })
}