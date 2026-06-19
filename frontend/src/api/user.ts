import request from '../utils/request';

export function login(data: any) {
  return request({
    url: '/user/login',
    method: 'post',
    data,
  });
}

export function register(data: any) {
  return request({
    url: '/user/register',
    method: 'post',
    data,
  });
}

export function getUserInfo(id: number) {
  return request({
    url: `/user/${id}`,
    method: 'get',
  });
}

export function updateUser(data: any) {
  return request({
    url: '/user/update',
    method: 'post',
    data,
  });
}

export function uploadFile(formData: FormData) {
  return request({
    url: '/user/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

export function searchUsers(params: { keyword: string }) {
  return request({
    url: '/user/search',
    method: 'get',
    params,
  });
}
