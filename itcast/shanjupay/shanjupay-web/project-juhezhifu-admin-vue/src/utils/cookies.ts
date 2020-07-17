import Cookies from 'js-cookie'

// App
const sidebarStatusKey = 'sidebar_status'
export const getSidebarStatus = () => Cookies.get(sidebarStatusKey)
export const setSidebarStatus = (sidebarStatus: string) => Cookies.set(sidebarStatusKey, sidebarStatus)

// User
const tokenKey = 'vue_typescript_admin_access_token'
const id = 'tenantId'
export const getToken = () => Cookies.get(tokenKey)
export const getTenantId = () => Cookies.get(id)
export const setToken = (token: string) => Cookies.set(tokenKey, token)
export const setTenantId = (tenantId:number) => Cookies.set(id, String(tenantId))
export const removeToken = () => Cookies.remove(tokenKey)
 