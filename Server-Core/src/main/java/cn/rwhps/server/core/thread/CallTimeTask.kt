/*
 * Copyright 2020-2022 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package cn.rwhps.server.core.thread

enum class CallTimeTask(
    val group: String,
    val description: String
    ) {
    CallPingTask(                   "[Core]Server",   "Update player delay"),
    CallTeamTask(                   "[Core]Server",   "Update player team list"),
    CallCheckTask(                  "[Core]Server",   "Update player check data"),

    PlayerAfkTask(                  "[Core]Server",   "Transfer of authority"),
    GameOverTask(                   "[Core]Server",   "Check gameover"),
    VoteTask(                       "[Core]Server",   "Vote"),
    AutoStartTask(                  "[Corex]Server",  "Start automatically"),
    AutoUpdateMapsTask(             "[Corex]Server",  "Auto update maps"),
    AutoCheckTask(                  "[Corex]Server",  "Auto Check player survives"),


    CustomUpServerListTask(         "UpList",         "[Plugin UpList] Update Data"),
    UpServerListTask(               "UpList",         "Core"),
    UpServerListNewTask(            "UpList",         "Core"),


    BlackListCheckTask(             "[Netx]",         "超时去除"),
    ServerUploadDataTask(           "[RCN]",          "列表更新"),
    ServerUploadData_CheckTimeTask( "[RCN]",          "超时"),
    RelayRoom_CheckTimeTask(        "[RCN]",          "房间超时机制"),
    ServerStatusUpdate(             "[RCN]",          "状态更新"),
    ServerIDCheckUpdate(            "[RCN]",          "检查ID过期"),


    ServerUpStatistics(             "[RW-HPS]",       "统计数据更新")
}