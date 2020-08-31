package com.dhcc.copywork.controller;

import com.dhcc.copywork.entity.response.ResponseResult;
import com.dhcc.copywork.service.GameToPlayService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CopyWorkController {
    @Autowired
    private GameToPlayService gameToPlayService;

    @ApiOperation(value = "新增游戏", notes = "新增游戏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldGameId", value = "原始游戏id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "newGameID", value = "新的游戏id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "fre", value = "几分钟一注", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "gameName", value = "新的游戏名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gameCode", value = "新的游戏code", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "每期开始延迟几秒", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "stop", value = "每期结束提前几秒", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/add")
    public ResponseResult cp(@RequestParam("oldGameId") Integer oldGameId, @RequestParam("newGameID") Integer newGameID, @RequestParam("fre") Integer fre,
                             @RequestParam("gameName") String gameName, @RequestParam("gameCode") String gameCode, @RequestParam("start") Integer start,
                             @RequestParam("stop") Integer stop) throws Exception {

        ResponseResult responseResult = gameToPlayService.gameToPlay(oldGameId, newGameID, fre, gameName, gameCode, start, stop);
        return responseResult;
    }
    @ApiOperation(value = "清空并生成全部游戏", notes = "清空并生成全部游戏")
    @GetMapping("/addlist")
    public ResponseResult cplist() throws Exception{
        ResponseResult responseResult = gameToPlayService.copyGameAndPlayAndPlayCateAndGameTime();
        return responseResult;
    }
}
