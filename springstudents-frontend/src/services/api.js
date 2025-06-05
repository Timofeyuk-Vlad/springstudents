import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
});

export const fetchStudents = () => api.get('/students/details');
export const createStudent = (data) => api.post('/students', data);
export const updateStudent = (id, data) => api.put(`/students/${id}`, data);
export const deleteStudent = (id) => api.delete(`/students/${id}`);

export const fetchEvents = () => api.get('/events');
export const createEvent = (data) => api.post('/events', data);
export const updateEvent = (id, data) => api.put(`/events/${id}`, data);
export const deleteEvent = (id) => api.delete(`/events/${id}`);

export const fetchBarters = () => api.get('/barters');
export const createBarter = (data) => api.post('/barters', data);
export const updateBarter = (id, data) => api.put(`/barters/${id}`, data);
export const deleteBarter = (id) => api.delete(`/barters/${id}`);

// Добавьте обработчик ошибок
api.interceptors.response.use(
    response => response,
    error => {
        console.error('API Error:', error.response);
        return Promise.reject(error);
    }
);

export default api;