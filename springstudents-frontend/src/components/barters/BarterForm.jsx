import React, { useState, useEffect } from 'react'; // Добавлен импорт React и хуков
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
                form.setFieldsValue(barter);
            } else {
                form.resetFields();
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
            message.error('Ошибка: ' + error.response?.data?.message || error.message);
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
                <Form.Item name="item" label="Предмет" rules={[{ required: true }]}>
                    <Input />
                </Form.Item>
                <Form.Item name="description" label="Описание">
                    <Input.TextArea />
                </Form.Item>
                <Form.Item name="status" label="Статус" rules={[{ required: true }]}>
                    <Select>
                        <Option value="active">Активен</Option>
                        <Option value="closed">Завершен</Option>
                    </Select>
                </Form.Item>
                <Form.Item name="studentId" label="Студент" rules={[{ required: true }]}>
                    <Select>
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