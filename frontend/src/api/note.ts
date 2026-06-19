import request from '../utils/request';

export function getNoteList(params: any) {
  return request({
    url: '/note/list',
    method: 'get',
    params,
  });
}

export function getNoteDetail(id: number, params: any) {
  return request({
    url: `/note/${id}`,
    method: 'get',
    params,
  });
}

export function saveNote(data: any) {
  return request({
    url: '/note/save',
    method: 'post',
    data,
  });
}

export function likeNote(params: any) {
  return request({
    url: '/note/like',
    method: 'post',
    params,
  });
}

export function collectNote(params: any) {
  return request({
    url: '/note/collect',
    method: 'post',
    params,
  });
}

export function getComments(noteId: number) {
  return request({
    url: `/note/comments/${noteId}`,
    method: 'get',
  });
}

export function addComment(data: any) {
  return request({
    url: '/note/comment',
    method: 'post',
    data,
  });
}

export function getUserPublishedNotes(userId: number) {
  return request({
    url: '/note/user/published',
    method: 'get',
    params: { userId },
  });
}

export function getUserLikedNotes(userId: number) {
  return request({
    url: '/note/user/liked',
    method: 'get',
    params: { userId },
  });
}

export function getUserCollectedNotes(userId: number) {
  return request({
    url: '/note/user/collected',
    method: 'get',
    params: { userId },
  });
}
