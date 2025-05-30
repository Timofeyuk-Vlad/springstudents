import React, { useEffect } from 'react';
import { Modal, Form, Input, DatePicker, message } from 'antd';
import moment from 'moment';
import api from '../../services/api'; // Добавьте этот импорт

const StudentForm = ({ visible, onCancel, student, onSuccess }) => {
    const [form] = Form.useForm();

    useEffect(() => {
        if (visible) {
            if (student) {
                form.setFieldsValue({
                    ...student,
                    dateOfBirth: student.dateOfBirth ? moment(student.dateOfBirth) : null,
                });
            } else {
                form.resetFields();
            }
        }
    }, [visible, student, form]);

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            const data = {
                ...values,
                dateOfBirth: values.dateOfBirth.format('YYYY-MM-DD')
            };

            if (student) {
                await api.put(`/students/${student.id}`, data); // Используем импортированный api
                message.success('Студент обновлен');
            } else {
                await api.post('/students', data); // Используем импортированный api
                message.success('Студент добавлен');
            }

            onSuccess();
            onCancel();
        } catch (error) {
            console.error('Submission error:', error);
            message.error(`Ошибка: ${error.response?.data?.message || error.message}`);
        }
    };

    return (
        <Modal
            title={student ? 'Редактировать студента' : 'Добавить студента'}
            visible={visible}
            onOk={handleSubmit}
            onCancel={onCancel}
        >
            <Form form={form} layout="vertical">
                <Form.Item
                    name="firstName"
                    label="Имя"
                    rules={[{ required: true, message: 'Пожалуйста, введите имя' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="lastName"
                    label="Фамилия"
                    rules={[{ required: true, message: 'Пожалуйста, введите фамилию' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="email"
                    label="Email"
                    rules={[
                        { required: true, message: 'Пожалуйста, введите email' },
                        { type: 'email', message: 'Некорректный email' },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="dateOfBirth"
                    label="Дата рождения"
                    rules={[{ required: true, message: 'Пожалуйста, выберите дату рождения' }]}
                >
                    <DatePicker style={{ width: '100%' }} />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default StudentForm;