import React, { useState, useEffect } from 'react';
import { Modal, Form, Input, DatePicker, Select, message, Alert } from 'antd';
import dayjs from 'dayjs';
import { createEvent, updateEvent } from '../../services/api';
import isSameOrAfter from 'dayjs/plugin/isSameOrAfter';
dayjs.extend(isSameOrAfter);

const { Option } = Select;

const EventForm = ({ visible, onCancel, event, onSuccess, students = [] }) => {
    const [form] = Form.useForm();
    const [submitting, setSubmitting] = useState(false);
    const [formError, setFormError] = useState(null);

    useEffect(() => {
        if (visible) {
            setFormError(null);
            if (event && event.id) { // Редактирование
                form.setFieldsValue({
                    name: event.name || '',
                    description: event.description || '',
                    date: event.date ? dayjs(event.date) : null,
                    studentIds: event.studentIds || []
                });
            } else {
                form.resetFields();
                form.setFieldsValue({ date: dayjs().add(1, 'day').hour(10).minute(0).second(0) });
            }
        }
    }, [visible, event, form]);

    const handleSubmit = async () => {
        setFormError(null);
        try {
            const values = await form.validateFields();
            setSubmitting(true);

            const dateForBackend = values.date ? values.date.format('YYYY-MM-DDTHH:mm:ss') : null;

            const payload = {
                name: values.name,
                description: values.description || "",
                date: dateForBackend,
                studentIds: values.studentIds || []
            };

            console.log("Отправляемые данные:", payload);

            if (event && event.id) {
                await updateEvent(event.id, payload);
                message.success('Событие успешно обновлено!');
            } else {
                await createEvent(payload);
                message.success('Событие успешно создано!');
            }
            onSuccess();
        } catch (errorInfo) {
            if (errorInfo.errorFields && errorInfo.errorFields.length > 0) {
                setFormError('Пожалуйста, проверьте правильность заполнения всех обязательных полей.');
            } else {
                const apiErrorMessage = errorInfo.response?.data?.message ||
                    (errorInfo.response?.data?.details && errorInfo.response?.data?.details.join('; ')) ||
                    errorInfo.message ||
                    'Не удалось сохранить событие.';
                setFormError(apiErrorMessage);
                message.error(`Ошибка: ${apiErrorMessage}`);
            }
        } finally {
            setSubmitting(false);
        }
    };

    const validateDateNotInPast = (_, value) => {
        if (!value) {
            return Promise.resolve();
        }
        if (value.startOf('day').isBefore(dayjs().startOf('day'))) {
            return Promise.reject(new Error('Дата события не может быть в прошлом!'));
        }
        return Promise.resolve();
    };

    return (
        <Modal
            title={event && event.id ? 'Редактирование события' : 'Создание нового события'}
            open={visible}
            onOk={() => form.submit()}
            onCancel={onCancel}
            confirmLoading={submitting}
            destroyOnClose
            width={600}
            okText={event && event.id ? "Сохранить" : "Создать"}
            cancelText="Отмена"
        >
            <Form form={form} layout="vertical" name="event_form" onFinish={handleSubmit}>
                {formError && <Alert message={formError} type="error" showIcon style={{ marginBottom: 16 }} />}
                <Form.Item
                    name="name"
                    label="Название события"
                    rules={[{ required: true, message: 'Пожалуйста, введите название события' }]}
                >
                    <Input placeholder="Введите название события" />
                </Form.Item>
                <Form.Item
                    name="description"
                    label="Описание события"
                >
                    <Input.TextArea rows={3} placeholder="Добавьте описание события" />
                </Form.Item>
                <Form.Item
                    name="date"
                    label="Дата и время проведения"
                    rules={[
                        { required: true, message: 'Пожалуйста, укажите дату и время' },
                        { validator: validateDateNotInPast }
                    ]}
                >
                    <DatePicker
                        showTime={{ format: 'HH:mm' }}
                        format="DD.MM.YYYY HH:mm"
                        style={{ width: '100%' }}
                        placeholder="Выберите дату и время"
                    />
                </Form.Item>
                <Form.Item
                    name="studentIds"
                    label="Участники события"
                    rules={[
                        { required: true, message: 'Необходимо выбрать хотя бы одного участника' },
                        {
                            validator: (_, value) =>
                                value && value.length > 0
                                    ? Promise.resolve()
                                    : Promise.reject(new Error('Выберите хотя бы одного участника'))
                        }
                    ]}
                >
                    <Select
                        mode="multiple"
                        allowClear
                        style={{ width: '100%' }}
                        placeholder="Выберите участников из списка"
                        filterOption={(input, option) =>
                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                        }
                        options={students.map(student => ({
                            value: student.id,
                            label: `${student.firstName} ${student.lastName}`
                        }))}
                    />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default EventForm;