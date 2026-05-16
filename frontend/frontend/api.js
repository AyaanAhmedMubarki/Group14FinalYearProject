const API_BASE = 'http://localhost:8080';

function getAuth() {
  const raw = localStorage.getItem('auth');
  return raw ? JSON.parse(raw) : null;
}

function getToken() {
  const auth = getAuth();
  return auth ? auth.token : null;
}

function authHeaders() {
  const token = getToken();
  return token ? { 'Authorization': 'Bearer ' + token } : {};
}

function requireAuth(expectedRole) {
  const auth = getAuth();
  if (!auth) { window.location.href = 'Login.html'; return null; }
  if (expectedRole && auth.role !== expectedRole) {
    alert('Access denied. Please log in with the correct role.');
    window.location.href = 'Login.html';
    return null;
  }
  return auth;
}

function logout() {
  localStorage.removeItem('auth');
  window.location.href = 'Login.html';
}

function imgSrc(base64) {
  if (!base64) return 'https://via.placeholder.com/150';
  if (base64.startsWith('data:')) return base64;
  return 'data:image/jpeg;base64,' + base64;
}

async function apiGet(path) {
  const res = await fetch(API_BASE + path, {
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function apiPostJSON(path, body) {
  const res = await fetch(API_BASE + path, {
    method: 'POST',
    headers: { ...authHeaders(), 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function apiFormPost(path, formData) {
  const res = await fetch(API_BASE + path, {
    method: 'POST',
    headers: { ...authHeaders() },
    body: formData
  });
  if (!res.ok) throw new Error(await res.text());
  return res.text();
}

async function apiFormPut(path, formData) {
  const res = await fetch(API_BASE + path, {
    method: 'PUT',
    headers: { ...authHeaders() },
    body: formData
  });
  if (!res.ok) throw new Error(await res.text());
  return res.text();
}

async function apiDelete(path) {
  const res = await fetch(API_BASE + path, {
    method: 'DELETE',
    headers: { ...authHeaders() }
  });
  if (!res.ok) throw new Error(await res.text());
  return res.text();
}

function today() {
  return new Date().toISOString().split('T')[0];
}
