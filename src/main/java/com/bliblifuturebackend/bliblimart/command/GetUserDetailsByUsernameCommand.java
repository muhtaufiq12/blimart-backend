package com.bliblifuturebackend.bliblimart.command;

import com.blibli.oss.command.Command;
import com.bliblifuturebackend.bliblimart.model.entity.User;

public interface GetUserDetailsByUsernameCommand extends Command<String, User> {

}
