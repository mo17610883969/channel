import request from './request'

export function getUserPage(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export function getUserById(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function addUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function resetPassword(userId) {
  return request({
    url: '/user/reset-password',
    method: 'put',
    data: { userId }
  })
}

export function getUserRoles(userId) {
  return request({
    url: `/user/roles/${userId}`,
    method: 'get'
  })
}