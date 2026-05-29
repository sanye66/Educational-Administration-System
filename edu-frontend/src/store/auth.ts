import { create } from 'zustand';
import type { LoginUser } from '@/types';

interface AuthState {
  user: LoginUser | null;
  token: string | null;
  setAuth: (user: LoginUser) => void;
  clearAuth: () => void;
  isAuthenticated: () => boolean;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: JSON.parse(localStorage.getItem('user') || 'null'),
  token: localStorage.getItem('token'),

  setAuth: (user: LoginUser) => {
    localStorage.setItem('token', user.token);
    localStorage.setItem('user', JSON.stringify(user));
    set({ user, token: user.token });
  },

  clearAuth: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    set({ user: null, token: null });
  },

  isAuthenticated: () => {
    const { token } = get();
    return !!token;
  },
}));
