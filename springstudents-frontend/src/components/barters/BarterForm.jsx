import React, { useState, useEffect } from 'react';
import { Modal, Form, Input, Select, message } from 'antd';
import { fetchStudents, createBarter, updateBarter } from '../../services/api';

const { Option } = Select;

const BarterForm = ({ visible, onCancel, barter, onSuccess }) => {
    const [form] = Form.useForm();
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (visible) {
            loadStudents();
            if (barter) {
                // При редактировании устанавливаем текущие значения
                form.setFieldsValue(barter);
            } else {
                // При создании сбрасываем форму и устанавливаем статус "active"
                form.resetFields();
                form.setFieldsValue({
                    status: 'active'
                });
            }
        }
    }, [visible, barter, form]);

    const loadStudents = async () => {
        setLoading(true);
        try {
            const { data } = await fetchStudents();
            setStudents(data);
        } catch (error) {
            message.error('Ошибка загрузки студентов');
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            if (barter) {
                await updateBarter(barter.id, values);
                message.success('Обмен обновлен');
            } else {
                await createBarter(values);
                message.success('Обмен создан');
            }
            onSuccess();
            onCancel();
        } catch (error) {
            message.error('Ошибка: ' + (error.response?.data?.message || error.message));
        }
    };

    return (
        <Modal
            title={barter ? 'Редактировать обмен' : 'Добавить обмен'}
            visible={visible}
            onOk={handleSubmit}
            onCancel={onCancel}
            confirmLoading={loading}
        >
            <Form form={form} layout="vertical">
                <Form.Item
                    name="item"
                    label="Предмет"
                    rules={[{ required: true, message: 'Введите предмет обмена' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item name="description" label="Описание">
                    <Input.TextArea />
                </Form.Item>
                <Form.Item
                    name="status"
                    label="Статус"
                    rules={[{ required: true, message: 'Выберите статус' }]}
                >
                    <Select disabled={!barter}>
                        <Option value="active">Активен</Option>
                        <Option value="closed" disabled={!barter}>
                            Завершен
                        </Option>
                    </Select>
                </Form.Item>
                <Form.Item
                    name="studentId"
                    label="Студент"
                    rules={[{ required: true, message: 'Выберите студента' }]}
                >
                    <Select showSearch optionFilterProp="children">
                        {students.map((student) => (
                            <Option key={student.id} value={student.id}>
                                {student.firstName} {student.lastName}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default BarterForm;