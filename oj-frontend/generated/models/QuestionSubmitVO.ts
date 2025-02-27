/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JudgeInfo } from './JudgeInfo';
import type { UserVO } from './UserVO';
export type QuestionSubmitVO = {
    code?: string;
    createTime?: string;
    current?: number;
    id?: number;
    judgeInfo?: JudgeInfo;
    language?: string;
    pageSize?: number;
    questionId?: number;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    userId?: number;
    userVO?: UserVO;
};

