import React, { useEffect, useState } from 'react';
import { Modal, Form, Input, DatePicker, message } from 'antd';
import moment from 'moment';
import api from '../../services/api';

const StudentForm = ({ visible, onCancel, student, onSuccess }) => {
    const [form] = Form.useForm();
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        if (visible) {
            if (student && student.id) {
                setIsEditing(true);
                form.setFieldsValue({
                    ...student,
                    dateOfBirth: student.dateOfBirth ? moment(student.dateOfBirth) : null,
                });
            } else {
                setIsEditing(false);
                form.resetFields();
            }
        }
    }, [visible, student, form]);

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            const data = {
                ...values,
                dateOfBirth: values.dateOfBirth ? values.dateOfBirth.format('YYYY-MM-DD') : (isEditing ? student.dateOfBirth : null)
            };

            if (isEditing && student && student.dateOfBirth) {
                data.dateOfBirth = student.dateOfBirth;
            }

            if (isEditing && student && student.id) {
                await api.put(`/students/${student.id}`, data);
                message.success('Данные студента обновлены');
            } else {
                await api.post('/students', data);
                message.success('Студент добавлен');
            }

            onSuccess();
            onCancel();
        } catch (errorInfo) {
            if (errorInfo.errorFields && errorInfo.errorFields.length > 0) {
                console.error('Ошибка валидации формы:', errorInfo.errorFields);
                message.error('Пожалуйста, исправьте ошибки в форме.');
            } else {
                const errorMessage = errorInfo.response?.data?.message || errorInfo.message || 'Не удалось сохранить данные студента.';
                console.error('Ошибка отправки:', errorInfo);
                message.error(`Ошибка: ${errorMessage}`);
            }
        }
    };

    return (
        <Modal
            title={isEditing ? 'Редактирование студента' : 'Добавление нового студента'}
            open={visible}
            onOk={handleSubmit}
            onCancel={onCancel}
            okText="Ок"
            cancelText="Отмена"
            destroyOnClose
        >
            <Form form={form} layout="vertical" name="student_form_modal">
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
                        { type: 'email', message: 'Некорректный формат email' },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="dateOfBirth"
                    label="Дата рождения"
                    rules={isEditing ? [] : [{ required: true, message: 'Пожалуйста, укажите дату рождения' }]}
                >
                    <DatePicker
                        style={{ width: '100%' }}
                        format="YYYY-MM-DD"
                        disabledDate={current => current && current > moment().endOf('day')}
                    />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default StudentForm;